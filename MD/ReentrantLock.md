# ReentrantLock 实现原理 

使用 `synchronize` 来做同步处理时，锁的获取和释放都是隐式的，实现的原理是通过编译后加上不同的机器指令来实现的。

而 ReentrantLock