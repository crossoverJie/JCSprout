![1.jpeg](https://i.loli.net/2018/06/12/5b1fea79e07cb.jpeg)

## 前言

Google 出的 [Guava](https://github.com/google/guava) 是 Java 核心增强的库，应用非常广泛。

我平时用的也挺频繁，这次就借助日常使用的 Cache 组件来看看 Google 大牛们是如何设计的。

## 缓存

> 本次主要讨论缓存。

缓存在日常开发中举足轻重，如果你的应用对某类数据有着较高的读取频次，并且改动较小时那就非常适合利用缓存来提高性能。

缓存之所以可以提高性能是因为它的读取效率很高，就像是 CPU 的 `L1、L2、L3` 缓存一样，级别越高相应的读取速度也会越快。

但也不是什么好处都占，读取速度快了但是它的内存更小资源更宝贵，所以我们应当缓存真正需要的数据。

> 其实也就是典型的空间换时间。

下面谈谈 Java 中所用到的缓存。

<!--more-->

### JVM 缓存

首先是 JVM 缓存，也可以认为是堆缓存。

其实就是创建一些全局变量，如 `Map、List` 之类的容器用于存放数据。

这样的优势是使用简单但是也有以下问题：

- 只能显式的写入，清除数据。
- 不能按照一定的规则淘汰数据，如 `LRU，LFU，FIFO` 等。
- 清除数据时的回调通知。
- 其他一些定制功能等。

### Ehcache、Guava Cache

所以出现了一些专门用作 JVM 缓存的开源工具出现了，如本文提到的 Guava Cache。

它具有上文 JVM 缓存不具有的功能，如自动清除数据、多种清除算法、清除回调等。

但也正因为有了这些功能，这样的缓存必然会多出许多东西需要额外维护，自然也就增加了系统的消耗。

### 分布式缓存

刚才提到的两种缓存其实都是堆内缓存，只能在单个节点中使用，这样在分布式场景下就招架不住了。

于是也有了一些缓存中间件，如 Redis、Memcached，在分布式环境下可以共享内存。

具体不在本次的讨论范围。

## Guava Cache 示例

之所以想到 Guava 的 Cache，也是最近在做一个需求，大体如下：

> 从 Kafka 实时读取出应用系统的日志信息，该日志信息包含了应用的健康状况。
> 如果在时间窗口 N 内发生了 X 次异常信息，相应的我就需要作出反馈（报警、记录日志等）。

对此 Guava 的 Cache 就非常适合，我利用了它的 N 个时间内不写入数据时缓存就清空的特点，在每次读取数据时判断异常信息是否大于 X 即可。

伪代码如下：

```java

    @Value("${alert.in.time:2}")
    private int time ;

    @Bean
    public LoadingCache buildCache(){
        return CacheBuilder.newBuilder()
                .expireAfterWrite(time, TimeUnit.MINUTES)
                .build(new CacheLoader<Long, AtomicLong>() {
                    @Override
                    public AtomicLong load(Long key) throws Exception {
                        return new AtomicLong(0);
                    }
                });
    }
    
    
    /**
     * 判断是否需要报警
     */
    public void checkAlert() {
        try {
            if (counter.get(KEY).incrementAndGet() >= limit) {
                LOGGER.info("***********报警***********");

                //将缓存清空
                counter.get(KEY).getAndSet(0L);
            }
        } catch (ExecutionException e) {
            LOGGER.error("Exception", e);
        }
    }   
```

首先是构建了 LoadingCache 对象，在 N 分钟内不写入数据时就回收缓存（当通过 Key 获取不到缓存时，默认返回 0）。

然后在每次消费时候调用 `checkAlert()` 方法进行校验，这样就可以达到上文的需求。

我们来设想下 Guava 它是如何实现过期自动清除数据，并且是可以按照 LRU 这样的方式清除的。

大胆假设下：

> 内部通过一个队列来维护缓存的顺序，每次访问过的数据移动到队列头部，并且额外开启一个线程来判断数据是否过期，过期就删掉。有点类似于我之前写过的 [动手实现一个 LRU cache](https://crossoverjie.top/%2F2018%2F04%2F07%2Falgorithm%2FLRU-cache%2F)


胡适说过：大胆假设小心论证

下面来看看 Guava 到底是怎么实现。

### 原理分析

看原理最好不过是跟代码一步步走了：

示例代码在这里：

[https://github.com/crossoverJie/Java-Interview/blob/master/src/main/java/com/crossoverjie/guava/CacheLoaderTest.java](https://github.com/crossoverJie/Java-Interview/blob/master/src/main/java/com/crossoverjie/guava/CacheLoaderTest.java)

![8.png](https://i.loli.net/2018/06/13/5b2008f4c1003.png)


为了能看出 Guava 是怎么删除过期数据的在获取缓存之前休眠了 5 秒钟，达到了超时条件。

![2.png](https://i.loli.net/2018/06/13/5b1ffe4eebae0.png)

最终会发现在 `com.google.common.cache.LocalCache` 类的 2187 行比较关键。

再跟进去之前第 2182 行会发现先要判断 count 是否大于 0，这个 count 保存的是当前缓存的数量，并用 volatile 修饰保证了可见性。

> 更多关于 volatile 的相关信息可以查看 [你应该知道的 volatile 关键字](https://crossoverjie.top/%2F2018%2F03%2F09%2Fvolatile%2F)


接着往下跟到：

![3.png](https://i.loli.net/2018/06/13/5b1fffc88c3e6.png)

2761 行，根据方法名称可以看出是判断当前的 Entry 是否过期，该 entry 就是通过 key 查询到的。


![](https://ws2.sinaimg.cn/large/006tNc79gy1ft9l0mx77rj30zk0a1tat.jpg)

这里就很明显的看出是根据根据构建时指定的过期方式来判断当前 key 是否过期了。

![5.png](https://i.loli.net/2018/06/13/5b20017f32ff0.png)

如果过期就往下走，尝试进行过期删除（需要加锁，后面会具体讨论）。

![6.png](https://i.loli.net/2018/06/13/5b2001eeb40d5.png)

到了这里也很清晰了：

- 获取当前缓存的总数量
- 自减一（前面获取了锁，所以线程安全）
- 删除并将更新的总数赋值到 count。

其实大体上就是这个流程，Guava 并没有按照之前猜想的另起一个线程来维护过期数据。

应该是以下原因：

- 新起线程需要资源消耗。
- 维护过期数据还要获取额外的锁，增加了消耗。

而在查询时候顺带做了这些事情，但是如果该缓存迟迟没有访问也会存在数据不能被回收的情况，不过这对于一个高吞吐的应用来说也不是问题。

## 总结

最后再来总结下 Guava 的 Cache。

其实在上文跟代码时会发现通过一个 key 定位数据时有以下代码：

![7.png](https://i.loli.net/2018/06/13/5b20040d257cb.png)

如果有看过 [ConcurrentHashMap 的原理](https://github.com/crossoverJie/Java-Interview/blob/master/MD/ConcurrentHashMap.md) 应该会想到这其实非常类似。

其实 Guava Cache 为了满足并发场景的使用，核心的数据结构就是按照 ConcurrentHashMap 来的，这里也是一个 key 定位到一个具体位置的过程。

> 先找到 Segment，再找具体的位置，等于是做了两次 Hash 定位。

上文有一个假设是对的，它内部会维护两个队列 `accessQueue,writeQueue` 用于记录缓存顺序，这样才可以按照顺序淘汰数据（类似于利用 LinkedHashMap 来做 LRU 缓存）。

同时从上文的构建方式来看，它也是[构建者模式](https://crossoverjie.top/2018/04/28/sbc/sbc7-Distributed-Limit/)来创建对象的。

因为作为一个给开发者使用的工具，需要有很多的自定义属性，利用构建则模式再合适不过了。

Guava 其实还有很多东西没谈到，比如它利用 GC 来回收内存，移除数据时的回调通知等。之后再接着讨论。

扫码关注微信公众号，第一时间获取消息。



## 进一步分析

## 前言

在上文「[Guava 源码分析（Cache 原理）](https://crossoverjie.top/2018/06/13/guava/guava-cache/)」中分析了 `Guava Cache` 的相关原理。

文末提到了**回收机制、移除时间通知**等内容，许多朋友也挺感兴趣，这次就这两个内容再来分析分析。


> 在开始之前先补习下 Java 自带的两个特性，Guava 中都有具体的应用。

## Java 中的引用

首先是 Java 中的**引用**。

在之前分享过 JVM 是根据[可达性分析算法](https://github.com/crossoverJie/Java-Interview/blob/master/MD/GarbageCollection.md#%E5%8F%AF%E8%BE%BE%E6%80%A7%E5%88%86%E6%9E%90%E7%AE%97%E6%B3%95)找出需要回收的对象，判断对象的存活状态都和`引用`有关。

在 JDK1.2 之前这点设计的非常简单：一个对象的状态只有**引用**和**没被引用**两种区别。

<!--more-->

这样的划分对垃圾回收不是很友好，因为总有一些对象的状态处于这两之间。

因此 1.2 之后新增了四种状态用于更细粒度的划分引用关系：

- 强引用（Strong Reference）:这种对象最为常见，比如 **`A a = new A();`**这就是典型的强引用；这样的强引用关系是不能被垃圾回收的。
- 软引用（Soft Reference）:这样的引用表明一些有用但不是必要的对象，在将发生垃圾回收之前是需要将这样的对象再次回收。
- 弱引用（Weak Reference）:这是一种比软引用还弱的引用关系，也是存放非必须的对象。当垃圾回收时，无论当前内存是否足够，这样的对象都会被回收。
- 虚引用（Phantom Reference）:这是一种最弱的引用关系，甚至没法通过引用来获取对象，它唯一的作用就是在被回收时可以获得通知。

## 事件回调

事件回调其实是一种常见的设计模式，比如之前讲过的 [Netty](https://crossoverjie.top/categories/Netty/) 就使用了这样的设计。

这里采用一个 demo，试下如下功能：

- Caller 向 Notifier 提问。
- 提问方式是异步，接着做其他事情。
- Notifier 收到问题执行计算然后回调 Caller 告知结果。

在 Java 中利用接口来实现回调，所以需要定义一个接口：

```java
public interface CallBackListener {

    /**
     * 回调通知函数
     * @param msg
     */
    void callBackNotify(String msg) ;
}
```

Caller 中调用 Notifier 执行提问，调用时将接口传递过去：

```java
public class Caller {

    private final static Logger LOGGER = LoggerFactory.getLogger(Caller.class);

    private CallBackListener callBackListener ;

    private Notifier notifier ;

    private String question ;

    /**
     * 使用
     */
    public void call(){

        LOGGER.info("开始提问");

		//新建线程，达到异步效果 
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    notifier.execute(Caller.this,question);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        LOGGER.info("提问完毕，我去干其他事了");
    }
    
    //隐藏 getter/setter
    
}    
```

Notifier 收到提问，执行计算（耗时操作），最后做出响应（回调接口，告诉 Caller 结果）。


```java
public class Notifier {

    private final static Logger LOGGER = LoggerFactory.getLogger(Notifier.class);

    public void execute(Caller caller, String msg) throws InterruptedException {
        LOGGER.info("收到消息=【{}】", msg);

        LOGGER.info("等待响应中。。。。。");
        TimeUnit.SECONDS.sleep(2);


        caller.getCallBackListener().callBackNotify("我在北京！");

    }

}
```


模拟执行：

```java
    public static void main(String[] args) {
        Notifier notifier = new Notifier() ;

        Caller caller = new Caller() ;
        caller.setNotifier(notifier) ;
        caller.setQuestion("你在哪儿！");
        caller.setCallBackListener(new CallBackListener() {
            @Override
            public void callBackNotify(String msg) {
                LOGGER.info("回复=【{}】" ,msg);
            }
        });

        caller.call();
    }
```

最后执行结果：

```log
2018-07-15 19:52:11.105 [main] INFO  c.crossoverjie.guava.callback.Caller - 开始提问
2018-07-15 19:52:11.118 [main] INFO  c.crossoverjie.guava.callback.Caller - 提问完毕，我去干其他事了
2018-07-15 19:52:11.117 [Thread-0] INFO  c.c.guava.callback.Notifier - 收到消息=【你在哪儿！】
2018-07-15 19:52:11.121 [Thread-0] INFO  c.c.guava.callback.Notifier - 等待响应中。。。。。
2018-07-15 19:52:13.124 [Thread-0] INFO  com.crossoverjie.guava.callback.Main - 回复=【我在北京！】
```

这样一个模拟的异步事件回调就完成了。

## Guava 的用法

Guava 就是利用了上文的两个特性来实现了**引用回收**及**移除通知**。

### 引用

可以在初始化缓存时利用：

- CacheBuilder.weakKeys()
- CacheBuilder.weakValues()
- CacheBuilder.softValues()

来自定义键和值的引用关系。

![](https://ws2.sinaimg.cn/large/006tKfTcgy1ftatngp76aj30n20h6gpn.jpg)

在上文的分析中可以看出 Cache 中的 `ReferenceEntry` 是类似于 HashMap 的 Entry 存放数据的。

来看看 ReferenceEntry 的定义：

```java
  interface ReferenceEntry<K, V> {
    /**
     * Returns the value reference from this entry.
     */
    ValueReference<K, V> getValueReference();

    /**
     * Sets the value reference for this entry.
     */
    void setValueReference(ValueReference<K, V> valueReference);

    /**
     * Returns the next entry in the chain.
     */
    @Nullable
    ReferenceEntry<K, V> getNext();

    /**
     * Returns the entry's hash.
     */
    int getHash();

    /**
     * Returns the key for this entry.
     */
    @Nullable
    K getKey();

    /*
     * Used by entries that use access order. Access entries are maintained in a doubly-linked list.
     * New entries are added at the tail of the list at write time; stale entries are expired from
     * the head of the list.
     */

    /**
     * Returns the time that this entry was last accessed, in ns.
     */
    long getAccessTime();

    /**
     * Sets the entry access time in ns.
     */
    void setAccessTime(long time);
}
```

包含了很多常用的操作，如值引用、键引用、访问时间等。

根据 `ValueReference<K, V> getValueReference();` 的实现：

![](https://ws1.sinaimg.cn/large/006tKfTcgy1ftatsg5jfvj30vg059wg9.jpg)

具有强引用和弱引用的不同实现。

key 也是相同的道理：

![](https://ws2.sinaimg.cn/large/006tKfTcgy1ftattls2uzj30w005eq4t.jpg)

当使用这样的构造方式时，弱引用的 key 和 value 都会被垃圾回收。

当然我们也可以显式的回收：

```
  /**
   * Discards any cached value for key {@code key}.
   * 单个回收
   */
  void invalidate(Object key);

  /**
   * Discards any cached values for keys {@code keys}.
   *
   * @since 11.0
   */
  void invalidateAll(Iterable<?> keys);

  /**
   * Discards all entries in the cache.
   */
  void invalidateAll();
```

### 回调

改造了之前的例子：

```java
loadingCache = CacheBuilder.newBuilder()
        .expireAfterWrite(2, TimeUnit.SECONDS)
        .removalListener(new RemovalListener<Object, Object>() {
            @Override
            public void onRemoval(RemovalNotification<Object, Object> notification) {
                LOGGER.info("删除原因={}，删除 key={},删除 value={}",notification.getCause(),notification.getKey(),notification.getValue());
            }
        })
        .build(new CacheLoader<Integer, AtomicLong>() {
            @Override
            public AtomicLong load(Integer key) throws Exception {
                return new AtomicLong(0);
            }
        });
```

执行结果：

```log
2018-07-15 20:41:07.433 [main] INFO  c.crossoverjie.guava.CacheLoaderTest - 当前缓存值=0,缓存大小=1
2018-07-15 20:41:07.442 [main] INFO  c.crossoverjie.guava.CacheLoaderTest - 缓存的所有内容={1000=0}
2018-07-15 20:41:07.443 [main] INFO  c.crossoverjie.guava.CacheLoaderTest - job running times=10
2018-07-15 20:41:10.461 [main] INFO  c.crossoverjie.guava.CacheLoaderTest - 删除原因=EXPIRED，删除 key=1000,删除 value=1
2018-07-15 20:41:10.462 [main] INFO  c.crossoverjie.guava.CacheLoaderTest - 当前缓存值=0,缓存大小=1
2018-07-15 20:41:10.462 [main] INFO  c.crossoverjie.guava.CacheLoaderTest - 缓存的所有内容={1000=0}
```

可以看出当缓存被删除的时候会回调我们自定义的函数，并告知删除原因。

那么 Guava 是如何实现的呢？

![](https://ws3.sinaimg.cn/large/006tKfTcgy1ftau23uj5aj30mp08odh8.jpg)

根据 LocalCache 中的 `getLiveValue()` 中判断缓存过期时，跟着这里的调用关系就会一直跟到：

![](https://ws1.sinaimg.cn/large/006tKfTcgy1ftau4ed7dcj30rm0a5acd.jpg)

`removeValueFromChain()` 中的：

![](https://ws1.sinaimg.cn/large/006tKfTcgy1ftau5ywcojj30rs0750u9.jpg)

`enqueueNotification()` 方法会将回收的缓存（包含了 key，value）以及回收原因包装成之前定义的事件接口加入到一个**本地队列**中。

![](https://ws4.sinaimg.cn/large/006tKfTcgy1ftau7hpijrj30sl06wtaf.jpg)

这样一看也没有回调我们初始化时候的事件啊。

不过用过队列的同学应该能猜出，既然这里写入队列，那就肯定就有消费。

我们回到获取缓存的地方：

![](https://ws1.sinaimg.cn/large/006tKfTcgy1ftau9rwgacj30ti0hswio.jpg)

在 finally 中执行了 `postReadCleanup()` 方法；其实在这里面就是对刚才的队列进行了消费：

![](https://ws1.sinaimg.cn/large/006tKfTcgy1ftaubaco48j30lw0513zi.jpg)

一直跟进来就会发现这里消费了队列，将之前包装好的移除消息调用了我们自定义的事件，这样就完成了一次事件回调。

## 总结

以上所有源码：

[https://github.com/crossoverJie/Java-Interview/blob/master/src/main/java/com/crossoverjie/guava/callback/Main.java](https://github.com/crossoverJie/Java-Interview/blob/master/src/main/java/com/crossoverjie/guava/callback/Main.java)

通过分析 Guava 的源码可以让我们学习到顶级的设计及实现方式，甚至自己也能尝试编写。

Guava 里还有很多强大的增强实现，值得我们再好好研究。
