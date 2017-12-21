# Java 多线程三大核心

![](https://ws2.sinaimg.cn/large/006tKfTcly1fmouu3fpokj31ae0osjt1.jpg)

## 原子性
Java 的原子性就和数据库事物的原子性差不多，一个操作中要么全部执行成功或者失败。

`JMM` 只是基本的原子性，类似于 `i++` 之类的操作，看似是原子操作，其实里面涉及到:

- 获取 i 的值。
- 自增。
- 再赋值给 i.

这三步操作，所以想要实现 `i++` 这样的操作就需要用到 `synchronize` 或者是 `lock` 进行加锁处理。

如果是基础类的自增操作可以使用 `AtomicInteger` 这样的原子类来实现。


## 可见性

## 顺序性