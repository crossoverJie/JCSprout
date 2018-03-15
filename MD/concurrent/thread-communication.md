# 深入理解线程通信

## 前言

开发中不免会遇到需要所有子线程执行完毕通知主线程处理某些逻辑的场景。

或者是线程 A 在执行到某个条件通知线程 B 执行某个操作。

可以通过以下几种方式实现：


## 等待通知机制
> 等待通知模式是 Java 中比较经典的线程通信方式。

两个线程通过对同一对象调用等待 wait() 和通知 notify() 方法来进行通讯。

如两个线程交替打印奇偶数：

```java
public class TwoThreadWaitNotify {

    private int start = 1;

    private boolean flag = false;

    public static void main(String[] args) {
        TwoThreadWaitNotify twoThread = new TwoThreadWaitNotify();

        Thread t1 = new Thread(new OuNum(twoThread));
        t1.setName("A");


        Thread t2 = new Thread(new JiNum(twoThread));
        t2.setName("B");

        t1.start();
        t2.start();
    }

    /**
     * 偶数线程
     */
    public static class OuNum implements Runnable {
        private TwoThreadWaitNotify number;

        public OuNum(TwoThreadWaitNotify number) {
            this.number = number;
        }

        @Override
        public void run() {

            while (number.start <= 100) {
                synchronized (TwoThreadWaitNotify.class) {
                    System.out.println("偶数线程抢到锁了");
                    if (number.flag) {
                        System.out.println(Thread.currentThread().getName() + "+-+偶数" + number.start);
                        number.start++;

                        number.flag = false;
                        TwoThreadWaitNotify.class.notify();

                    }else {
                        try {
                            TwoThreadWaitNotify.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
    }


    /**
     * 奇数线程
     */
    public static class JiNum implements Runnable {
        private TwoThreadWaitNotify number;

        public JiNum(TwoThreadWaitNotify number) {
            this.number = number;
        }

        @Override
        public void run() {
            while (number.start <= 100) {
                synchronized (TwoThreadWaitNotify.class) {
                    System.out.println("奇数线程抢到锁了");
                    if (!number.flag) {
                        System.out.println(Thread.currentThread().getName() + "+-+奇数" + number.start);
                        number.start++;

                        number.flag = true;

                        TwoThreadWaitNotify.class.notify();
                    }else {
                        try {
                            TwoThreadWaitNotify.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
```

这里的线程 A 和线程 B 都对同一个对象 `TwoThreadWaitNotify.class` 获取锁，A 线程调用了同步对象的 wait() 方法释放了锁并进入 `WAITING` 状态。

B 线程调用了 notify() 方法，这样 A 线程收到通知之后就可以从 wait() 方法中返回。利用了 `TwoThreadWaitNotify.class` 对象完成了交互。

这里有一些注意点:

- wait() 、nofify() 、nofityAll() 调用的前提都是获得了对象的锁(也可成为对象监视器)。
- 调用 wait() 方法后线程会释放锁，进入 `WAITING` 状态，该线程也会被移动到**等待队列**中。
- 调用 notify() 方法会将**等待队列**中的线程移动到**同步队列**中，线程状态也会更新为 `BLOCKED`
- 从 wait() 方法返回的前提是调用 notify() 方法的线程释放锁，wait() 方法的线程获得锁。

等待通知有着一个经典范式：

线程 A 作为消费者：

1. 获取对象的锁。
2. 进入 while(判断条件)，并调用 wait() 方法。
3. 当条件满足跳出循环执行具体处理逻辑。

线程 B 作为生产者:

1. 获取对象锁。
2. 更改与线程 A 共用的判断条件。
3. 调用 notify() 方法。

伪代码如下:

```
//Thread A

synchronized(Object){
    while(条件){
        Object.wait();
    }
    //do something
}

//Thread B
synchronized(Object){
    条件=false;//改变条件
    Object.notify();
}

```


## join() 方法

```java
    private static void join() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("running");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }) ;
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("running2");
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }) ;

        t1.start();
        t2.start();

        //等待线程1终止
        t1.join();

        //等待线程2终止
        t2.join();

        LOGGER.info("main over");
    }
```

在  `t1.join()` 时会一直阻塞到 t1 执行完毕，所以最终主线程会等待 t1 和 t2 线程执行完毕。

其实从源码可以看出，join() 也是利用的等待通知机制：

核心逻辑:

```java
    while (isAlive()) {
        wait(0);
    }
```

在 join 线程完成后会调用 notifyAll() 方法，是在 JVM 实现中调用，所以这里看不出来。

## volatile 共享内存

## CountDownLatch 并发工具

## 线程响应中断

## 线程池 awaitTermination() 方法

## 管道通信