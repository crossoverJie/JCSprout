# 线程池原理分析

首先要明确为什么要使用线程池，使用线程池会带来什么好处？

- 线程是稀缺资源，不能频繁的创建。
- 应当将其放入一个池子中，可以给其他任务进行复用。
- 解耦作用，线程的创建于执行完全分开，方便维护。


## 创建一个线程池

以一个使用较多的 
`ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) ` 为例：

- 其中的 `corePoolSize` 为线程池的基本大小。
- `maximumPoolSize` 为线程池最大线程大小。
- `keepAliveTime` 和 `unit` 则是线程空闲后的存活时间。
- `workQueue` 用于存放任务的阻塞队列。
- `handler` 当队列和最大线程池都满了之后的饱和策略。

## 处理流程
当提交一个任务到线程池时它的执行流程是怎样的呢？

![](https://ws1.sinaimg.cn/large/006tNbRwgy1fnbzmai8yrj30dw08574s.jpg)

首先第一步会判断核心