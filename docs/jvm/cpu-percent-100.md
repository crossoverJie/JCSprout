

![](https://ws3.sinaimg.cn/large/006tNbRwly1fy67gauqxyj31eg0u0gun.jpg)

# 前言

到了年底果然都不太平，最近又收到了运维报警：表示有些服务器负载非常高，让我们定位问题。

还真是想什么来什么，前些天还故意把某些服务器的负载提高（[没错，老板让我写个 BUG！](https://crossoverjie.top/2018/12/12/java-senior/java-memary-allocation/)），不过还好是不同的环境互相没有影响。




# 定位问题

拿到问题后首先去服务器上看了看，发现运行的只有我们的 Java 应用。于是先用 `ps` 命令拿到了应用的 `PID`。

接着使用 `top -Hp pid` 将这个进程的线程显示出来。输入大写的 P 可以将线程按照 CPU 使用比例排序，于是得到以下结果。

![](https://ws3.sinaimg.cn/large/006tNbRwly1fy7z1kg8s3j30s40ncn9w.jpg)

果然某些线程的 CPU 使用率非常高。


为了方便定位问题我立马使用 `jstack pid > pid.log` 将线程栈 `dump` 到日志文件中。

我在上面 100% 的线程中随机选了一个 `pid=194283` 转换为 16 进制（2f6eb）后在线程快照中查询：

> 因为线程快照中线程 ID 都是16进制存放。

![](https://ws1.sinaimg.cn/large/006tNbRwly1fy7z7vtcruj30q5056tar.jpg)

发现这是 `Disruptor` 的一个堆栈，前段时间正好解决过一个由于 Disruptor 队列引起的一次 [OOM]()：[强如 Disruptor 也发生内存溢出？](https://crossoverjie.top/2018/08/29/java-senior/OOM-Disruptor/)

没想到又来一出。

为了更加直观的查看线程的状态信息，我将快照信息上传到专门分析的平台上。

[http://fastthread.io/](http://fastthread.io/)

![](https://ws2.sinaimg.cn/large/006tNbRwly1fy7zciqp2ij311q0q5jzl.jpg)

其中有一项菜单展示了所有消耗 CPU 的线程，我仔细看了下发现几乎都是和上面的堆栈一样。

也就是说都是 `Disruptor` 队列的堆栈，同时都在执行 `java.lang.Thread.yield` 函数。

众所周知 `yield` 函数会让当前线程让出 `CPU` 资源，再让其他线程来竞争。

根据刚才的线程快照发现处于 `RUNNABLE` 状态并且都在执行 `yield` 函数的线程大概有 30几个。

因此初步判断为大量线程执行 `yield` 函数之后互相竞争导致 CPU 使用率增高，而通过对堆栈发现是和使用 `Disruptor` 有关。

# 解决问题

而后我查看了代码，发现是根据每一个业务场景在内部都会使用 2 个 `Disruptor` 队列来解耦。

假设现在有 7 个业务类型，那就等于是创建 `2*7=14` 个 `Disruptor` 队列，同时每个队列有一个消费者，也就是总共有 14 个消费者（生产环境更多）。

同时发现配置的消费等待策略为 `YieldingWaitStrategy` 这种等待策略确实会执行 yield 来让出 CPU。

代码如下：

![](https://ws3.sinaimg.cn/large/006tNbRwly1fy8yrlsituj30nv0nfq5d.jpg)

> 初步看来和这个等待策略有很大的关系。

## 本地模拟

为了验证，我在本地创建了 15 个 `Disruptor` 队列同时结合监控观察 CPU 的使用情况。

![](https://ws3.sinaimg.cn/large/006tNbRwly1fy8wd8puupj30s10bs0up.jpg)
![](https://ws1.sinaimg.cn/large/006tNbRwly1fy8weciz9jj30po03z0tk.jpg)

创建了 15 个 `Disruptor` 队列，同时每个队列都用线程池来往 `Disruptor队列` 里面发送 100W 条数据。

消费程序仅仅只是打印一下。

![](https://ws2.sinaimg.cn/large/006tNbRwly1fy8whdcy5hj30e706tdg7.jpg)

跑了一段时间发现 CPU 使用率确实很高。

---

![](https://ws4.sinaimg.cn/large/006tNbRwly1fy8wjq0xkwj310t0cln12.jpg)

同时 `dump` 线程发现和生产的现象也是一致的：消费线程都处于 `RUNNABLE` 状态，同时都在执行 `yield`。

通过查询 `Disruptor` 官方文档发现：

![](https://ws4.sinaimg.cn/large/006tNbRwly1fy8wx1x6z8j30l1069jsz.jpg)

> YieldingWaitStrategy 是一种充分压榨 CPU 的策略，使用`自旋 + yield`的方式来提高性能。
> 当消费线程（Event Handler threads）的数量小于 CPU 核心数时推荐使用该策略。

---

![](https://ws3.sinaimg.cn/large/006tNbRwly1fy8wym9wxlj30ln04sjsm.jpg)

同时查阅到其他的等待策略 `BlockingWaitStrategy` （也是默认的策略），它使用的是锁的机制，对 CPU 的使用率不高。

于是在和之前同样的条件下将等待策略换为 `BlockingWaitStrategy`。

![](https://ws3.sinaimg.cn/large/006tNbRwly1fy8x3b5xh7j30pl0brgnh.jpg)

---

![](https://ws1.sinaimg.cn/large/006tNbRwly1fy8x6jytcoj30e605b3yt.jpg)
![](https://ws3.sinaimg.cn/large/006tNbRwly1fy8x79u64nj30t6076jty.jpg)

和刚才的 CPU 对比会发现到后面使用率的会有明显的降低；同时 dump 线程后会发现大部分线程都处于 waiting 状态。


## 优化解决

看样子将等待策略换为 `BlockingWaitStrategy` 可以减缓 CPU 的使用，

但留意到官方对 `YieldingWaitStrategy` 的描述里谈道：
当消费线程（Event Handler threads）的数量小于 CPU 核心数时推荐使用该策略。

而现有的使用场景很明显消费线程数已经大大的超过了核心 CPU 数了，因为我的使用方式是一个 `Disruptor` 队列一个消费者，所以我将队列调整为只有 1 个再试试(策略依然是 `YieldingWaitStrategy`)。

![](https://ws3.sinaimg.cn/large/006tNbRwly1fy8xlhzh05j30qo0aogng.jpg)

![](https://ws2.sinaimg.cn/large/006tNbRwly1fy8xn1ktk6j30e207g0t0.jpg)

跑了一分钟，发现 CPU 的使用率一直都比较平稳而且不高。

# 总结

所以排查到此可以有一个结论了，想要根本解决这个问题需要将我们现有的业务拆分；现在是一个应用里同时处理了 N 个业务，每个业务都会使用好几个 `Disruptor` 队列。

由于是在一台服务器上运行，所以 CPU 资源都是共享的，这就会导致 CPU 的使用率居高不下。

所以我们的调整方式如下：

- 为了快速缓解这个问题，先将等待策略换为 `BlockingWaitStrategy`，可以有效降低 CPU 的使用率（业务上也还能接受）。
- 第二步就需要将应用拆分（上文模拟的一个 `Disruptor` 队列），一个应用处理一种业务类型；然后分别单独部署，这样也可以互相隔离互不影响。

当然还有其他的一些优化，因为这也是一个老系统了，这次 dump 线程居然发现创建了 800+ 的线程。

创建线程池的方式也是核心线程数、最大线程数是一样的，导致一些空闲的线程也得不到回收；这样会有很多无意义的资源消耗。

所以也会结合业务将创建线程池的方式调整一下，将线程数降下来，尽量的物尽其用。


本文的演示代码已上传至 GitHub：

[https://github.com/crossoverJie/JCSprout](https://github.com/crossoverJie/JCSprout/tree/master/src/main/java/com/crossoverjie/disruptor)

**你的点赞与分享是对我最大的支持**

![](https://ws2.sinaimg.cn/large/006tNbRwly1fyrjtr3ja2j30760760t7.jpg)