# 你应该知道的 volatile 关键字

## 前言

不管是在面试还是实际开发中 `volatile` 都是一个应该掌握的技能。

首先来看看为什么会出现这个关键字。

## 内存可见性
由于 `Java` 内存模型(`JMM`)规定，所有的变量都存放在主内存中，而每个线程都有着自己的工作内存(高速缓存)。

线程在工作时，需要将主内存中的数据拷贝到工作内存中。这样对数据的任何操作都是基于工作内存(效率提高)，并且不能直接操作主内存以及其他线程工作内存中的数据，之后再将更新之后的数据刷新到主内存中。

> 这里所提到的主内存可以简单认为是**堆内存**，而工作内存则可以认为是**栈内存**。

如下图所示：

![](https://ws2.sinaimg.cn/large/006tKfTcly1fmouu3fpokj31ae0osjt1.jpg)

所以在并发运行时可能会出现线程 B 所读取到的数据是线程 A 更新之前的数据。

显然这肯定是会出问题的，因此 `volatile` 的作用出现了：

> 当一个变量被 `volatile` 修饰时，任何线程对它的写操作都会立即刷新到主内存中，并且会强制让缓存了该变量的线程中的数据清空，必须从主内存重新读取最新数据。

*`volatile` 修饰之后并不是让线程直接从主内存中获取数据，依然需要将变量拷贝到工作内存中*。

### 内存可见性的应用 

当我们需要在两个线程间依据主内存通信时，通信的那个变量就必须的用 `volatile` 来修饰：

```java
public class Volatile implements Runnable{

    private static volatile boolean flag = true ;

    @Override
    public void run() {
        while (flag){
        }
        System.out.println(Thread.currentThread().getName() +"执行完毕");
    }

    public static void main(String[] args) throws InterruptedException {
        Volatile aVolatile = new Volatile();
        new Thread(aVolatile,"thread A").start();


        System.out.println("main 线程正在运行") ;

        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
            String value = sc.next();
            if(value.equals("1")){

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        aVolatile.stopThread();
                    }
                }).start();

                break ;
            }
        }

        System.out.println("主线程退出了！");

    }

    private void stopThread(){
        flag = false ;
    }

}
```

主线程在修改了标志位使得线程 A 立即停止，如果没有用 `volatile` 修饰，就有可能出现延迟。

但这里有个误区，这样的使用方式容易给人的感觉是：

> 对 `volatile` 修饰的变量进行并发操作是线程安全的。

这里要重点强调，`volatile` 并**不能**保证线程安全性！

如下程序:

```java
public class VolatileInc implements Runnable{

    private static volatile int count = 0 ; //使用 volatile 修饰基本数据内存不能保证原子性

    //private static AtomicInteger count = new AtomicInteger() ;

    @Override
    public void run() {
        for (int i=0;i<10000 ;i++){
            count ++ ;
            //count.incrementAndGet() ;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileInc volatileInc = new VolatileInc() ;
        Thread t1 = new Thread(volatileInc,"t1") ;
        Thread t2 = new Thread(volatileInc,"t2") ;
        t1.start();
        //t1.join();

        t2.start();
        //t2.join();
        for (int i=0;i<10000 ;i++){
            count ++ ;
            //count.incrementAndGet();
        }


        System.out.println("最终Count="+count);
    }
}
```

当我们三个线程(t1,t2,main)同时对一个 `int` 进行累加时会发现最终的值都会小于 30000。

> 这是因为虽然 `volatile` 保证了内存可见性，每个线程拿到的值都是最新值，但 `count ++` 这个操作并不是原子的，这里面涉及到获取值、自增、赋值的操作并不能同时完成。
> 

- 所以想到达到线程安全可以使这三个线程串行执行(其实就是单线程，没有发挥多线程的优势)。

- 也可以使用 `synchronized` 或者是锁的方式来保证原子性。
 
- 还可以用 `Atomic` 包中 `AtomicInteger` 来替换 `int`，它利用了 `CAS` 算法来保证了原子性。


## 指令重排

内存可见性只是 `volatile` 的其中一个语义，它还可以防止 `JVM` 进行指令重排优化。

举一个伪代码:

```java
int a=10 ;//1
int b=20 ;//2
int c= a+b ;//3
```

一段特别简单的代码，理想情况下它的执行顺序是：`1>2>3`。但有可能经过 JVM 优化之后的执行顺序变为了 `2>1>3`。

可以发现不管 JVM 怎么优化，前提都是保证单线程中最终结果不变的情况下进行的。

可能这里还看不出有什么问题，那看下一段伪代码:

```java
private static Map<String,String> value ;
private static volatile boolean flag = fasle ;

//以下方法发生在线程 A 中 初始化 Map
public void initMap(){
	//耗时操作
	value = getMapValue() ;//1
	flag = true ;//2
}


//发生在线程 B中 等到 Map 初始化成功进行其他操作
public void doSomeThing(){
	while(!flag){
		sleep() ;
	}
	//dosomething
	doSomeThing(value);
}

```

这里就能看出问题了，当 `flag` 没有被 `volatile` 修饰时，`JVM` 对 1 和 2 进行重排，导致 `value` 都还没有被初始化就有可能被线程 B 使用了。

所以加上 `volatile` 之后可以防止这样的重排优化，保证业务的正确性。
### 指令重排的的应用

一个经典的使用场景就是双重懒加载的单例模式了:

```java
public class Singleton {

    private static volatile Singleton singleton;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    //防止指令重排
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
```

这里的 `volatile` 关键字主要是为了防止指令重排。 

如果不用 ，`singleton = new Singleton();`，这段代码其实是分为三步：
- 分配内存空间。(1)
- 初始化对象。(2)
- 将 `singleton` 对象指向分配的内存地址。(3)

加上 `volatile` 是为了让以上的三步操作顺序执行，反之有可能第二步在第三步之前被执行就有可能某个线程拿到的单例对象是还没有初始化的，以致于报错。

## 总结

`volatile` 在 `Java` 并发中用的很多，比如像 `Atomic` 包中的 `value`、以及 `AbstractQueuedLongSynchronizer` 中的 `state` 都是被定义为 `volatile` 来用于保证内存可见性。

将这块理解透彻对我们编写并发程序时可以提供很大帮助。