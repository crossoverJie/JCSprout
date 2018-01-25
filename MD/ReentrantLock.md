# ReentrantLock 实现原理 

使用 `synchronize` 来做同步处理时，锁的获取和释放都是隐式的，实现的原理是通过编译后加上不同的机器指令来实现的。

而 `ReentrantLock` 就是一个普通的类，它是基于 `AQS(AbstractQueuedSynchronizer)`来实现的。

> `AQS` 是 `Java` 并发包里实现锁、同步的一个基础框架。