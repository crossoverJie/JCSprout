
<div align="center">  

<img src="https://s2.loli.net/2024/05/17/D8brxzCPh5vpOFk.png" width=""/> 
<br/>

[![Build Status](https://travis-ci.org/crossoverJie/JCSprout.svg?branch=master)](https://travis-ci.org/crossoverJie/JCSprout)
[![QQ群](https://img.shields.io/badge/QQ%E7%BE%A4-787381170-yellowgreen.svg)](https://jq.qq.com/?_wv=1027&k=5HPYvQk)
[![Maintainability](https://cloud.quality-gate.com/dashboard/api/badge?projectName=crossoverJie_JCSprout&branchName=master)](https://cloud.quality-gate.com/dashboard/branches/67235#overview)

[qq0groupsvg]: https://img.shields.io/badge/QQ%E7%BE%A4-787381170-yellowgreen.svg
[qq0group]: https://jq.qq.com/?_wv=1027&k=5HPYvQk

</div><br>


> `Java Core Sprout`：处于萌芽阶段的 Java 核心知识库。

**访问这里获取更好的阅读体验**：[https://crossoverjie.top/JCSprout/](https://crossoverjie.top/JCSprout/)

<br/>

<div align="center">  

<a href="https://t.zsxq.com/odQDJ" target="_blank"><img src="https://s2.loli.net/2024/05/17/zRkabDu2SKfChLX.png" alt="202405171520366.png"></a>
</div>

最近开通了知识星球，感谢大家对 `JCSprout` 的支持，为大家提供 100 份 10 元优惠券，也就是 69-10=59 元，具体福利大家可以扫码参考再决定是否加入。

> PS: 后续会继续维护该项目，同时加入现在热门的 Golang/kubernetes/OpenTelemetry 等知识点，感兴趣的可以加入星球当面催更（当然内容也会更新到这个项目里）。


| 📊 |⚔️ | 🖥 | 🚏 | 🏖  | 🌁| 📮 | 🔍 | 🚀 | 🌈 |💡
| :--------: | :---------: | :---------: | :---------: | :---------: | :---------:| :---------: | :-------: | :-------:| :------:|:------:|
| [集合](#常用集合) | [多线程](#java-多线程)|[JVM](#jvm) | [分布式](#分布式相关) |[框架](#常用框架第三方组件)|[架构设计](#架构设计)| [数据库](#db-相关) |[算法](#数据结构与算法)|[Netty](#netty-相关)| [附加技能](#附加技能)|[联系作者](#联系作者) |



### 常用集合
- [ArrayList/Vector](https://github.com/crossoverJie/JCSprout/blob/master/MD/ArrayList.md)
- [LinkedList](https://github.com/crossoverJie/JCSprout/blob/master/MD/LinkedList.md)
- [HashMap](https://github.com/crossoverJie/JCSprout/blob/master/MD/HashMap.md)
- [HashSet](https://github.com/crossoverJie/JCSprout/blob/master/MD/collection/HashSet.md)
- [LinkedHashMap](https://github.com/crossoverJie/JCSprout/blob/master/MD/collection/LinkedHashMap.md)

### Java 多线程
- [多线程中的常见问题](https://github.com/crossoverJie/JCSprout/blob/master/MD/Thread-common-problem.md)
- [synchronized 关键字原理](https://github.com/crossoverJie/JCSprout/blob/master/MD/Synchronize.md)
- [多线程的三大核心](https://github.com/crossoverJie/JCSprout/blob/master/MD/Threadcore.md)
- [对锁的一些认知](https://github.com/crossoverJie/JCSprout/blob/master/MD/Java-lock.md)
- [ReentrantLock 实现原理 ](https://github.com/crossoverJie/JCSprout/blob/master/MD/ReentrantLock.md)
- [ConcurrentHashMap 的实现原理](https://github.com/crossoverJie/JCSprout/blob/master/MD/ConcurrentHashMap.md)
- [如何优雅的使用和理解线程池](https://github.com/crossoverJie/JCSprout/blob/master/MD/ThreadPoolExecutor.md)
- [深入理解线程通信](https://github.com/crossoverJie/JCSprout/blob/master/MD/concurrent/thread-communication.md)
- [一个线程罢工的诡异事件](docs/thread/thread-gone.md)
- [线程池中你不容错过的一些细节](docs/thread/thread-gone2.md)
- [『并发包入坑指北』之阻塞队列](docs/thread/ArrayBlockingQueue.md)

### JVM
- [Java 运行时内存划分](https://github.com/crossoverJie/JCSprout/blob/master/MD/MemoryAllocation.md)
-  [类加载机制](https://github.com/crossoverJie/JCSprout/blob/master/MD/ClassLoad.md)
-  [OOM 分析](https://github.com/crossoverJie/JCSprout/blob/master/MD/OOM-analysis.md)
- [垃圾回收](https://github.com/crossoverJie/JCSprout/blob/master/MD/GarbageCollection.md)
- [对象的创建与内存分配](https://github.com/crossoverJie/JCSprout/blob/master/MD/newObject.md)
- [你应该知道的 volatile 关键字](https://github.com/crossoverJie/JCSprout/blob/master/MD/concurrent/volatile.md)
- [一次内存溢出排查优化实战](https://crossoverjie.top/2018/08/29/java-senior/OOM-Disruptor/)
- [一次 HashSet 所引起的并发问题](docs/jvm/JVM-concurrent-HashSet-problem.md)
- [一次生产 CPU 100% 排查优化实践](docs/jvm/cpu-percent-100.md)

### 分布式相关

- [分布式限流](http://crossoverjie.top/2018/04/28/sbc/sbc7-Distributed-Limit/)
- [基于 Redis 的分布式锁](http://crossoverjie.top/2018/03/29/distributed-lock/distributed-lock-redis/)
- [分布式缓存设计](https://github.com/crossoverJie/JCSprout/blob/master/MD/Cache-design.md)
- [分布式 ID 生成器](https://github.com/crossoverJie/JCSprout/blob/master/MD/ID-generator.md)

### 常用框架\第三方组件

- [Spring Bean 生命周期](https://github.com/crossoverJie/JCSprout/blob/master/MD/spring/spring-bean-lifecycle.md)
- [Spring AOP 的实现原理](https://github.com/crossoverJie/JCSprout/blob/master/MD/SpringAOP.md) 
- [Guava 源码分析（Cache 原理）](https://crossoverjie.top/2018/06/13/guava/guava-cache/)
- [轻量级 HTTP 框架](https://github.com/crossoverJie/cicada)
- [Kafka produce 源码分析](https://github.com/crossoverJie/JCSprout/blob/master/MD/kafka/kafka-product.md)
- [Kafka 消费实践](https://github.com/crossoverJie/JCSprout/blob/master/docs/frame/kafka-consumer.md)


### 架构设计
- [秒杀系统设计](https://github.com/crossoverJie/JCSprout/blob/master/MD/Spike.md)
- [秒杀架构实践](http://crossoverjie.top/2018/05/07/ssm/SSM18-seconds-kill/)
- [设计一个百万级的消息推送系统](https://github.com/crossoverJie/JCSprout/blob/master/MD/architecture-design/million-sms-push.md)

### DB 相关

- [MySQL 索引原理](https://github.com/crossoverJie/JCSprout/blob/master/MD/MySQL-Index.md)
- [SQL 优化](https://github.com/crossoverJie/JCSprout/blob/master/MD/SQL-optimization.md)
- [数据库水平垂直拆分](https://github.com/crossoverJie/JCSprout/blob/master/MD/DB-split.md)
- [一次分表踩坑实践的探讨](docs/db/sharding-db.md)

### 数据结构与算法
- [红包算法](https://github.com/crossoverJie/JCSprout/blob/master/src/main/java/com/crossoverjie/red/RedPacket.java)
- [二叉树层序遍历](https://github.com/crossoverJie/JCSprout/blob/master/src/main/java/com/crossoverjie/algorithm/BinaryNode.java#L76-L101)
- [是否为快乐数字](https://github.com/crossoverJie/JCSprout/blob/master/src/main/java/com/crossoverjie/algorithm/HappyNum.java#L38-L55)
- [链表是否有环](https://github.com/crossoverJie/JCSprout/blob/master/src/main/java/com/crossoverjie/algorithm/LinkLoop.java#L32-L59)
- [从一个数组中返回两个值相加等于目标值的下标](https://github.com/crossoverJie/JCSprout/blob/master/src/main/java/com/crossoverjie/algorithm/TwoSum.java#L38-L59)
- [一致性 Hash 算法原理](https://github.com/crossoverJie/JCSprout/blob/master/MD/Consistent-Hash.md)
- [一致性 Hash 算法实践](https://github.com/crossoverJie/JCSprout/blob/master/docs/algorithm/consistent-hash-implement.md)
- [限流算法](https://github.com/crossoverJie/JCSprout/blob/master/MD/Limiting.md)
- [三种方式反向打印单向链表](https://github.com/crossoverJie/JCSprout/blob/master/src/main/java/com/crossoverjie/algorithm/ReverseNode.java)
- [合并两个排好序的链表](https://github.com/crossoverJie/JCSprout/blob/master/src/main/java/com/crossoverjie/algorithm/MergeTwoSortedLists.java)
- [两个栈实现队列](https://github.com/crossoverJie/JCSprout/blob/master/src/main/java/com/crossoverjie/algorithm/TwoStackQueue.java)
- [动手实现一个 LRU cache](http://crossoverjie.top/2018/04/07/algorithm/LRU-cache/)
- [链表排序](./src/main/java/com/crossoverjie/algorithm/LinkedListMergeSort.java)
- [数组右移 k 次](./src/main/java/com/crossoverjie/algorithm/ArrayKShift.java)
- [交替打印奇偶数](https://github.com/crossoverJie/JCSprout/blob/master/src/main/java/com/crossoverjie/actual/TwoThread.java)
- [亿级数据中判断数据是否不存在](https://github.com/crossoverJie/JCSprout/blob/master/docs/algorithm/guava-bloom-filter.md) 

### Netty 相关
- [SpringBoot 整合长连接心跳机制](https://crossoverjie.top/2018/05/24/netty/Netty(1)TCP-Heartbeat/)
- [从线程模型的角度看 Netty 为什么是高性能的？](https://crossoverjie.top/2018/07/04/netty/Netty(2)Thread-model/)
- [为自己搭建一个分布式 IM(即时通讯) 系统](https://github.com/crossoverJie/cim)

### 附加技能

- [TCP/IP 协议](https://github.com/crossoverJie/JCSprout/blob/master/MD/TCP-IP.md)
- [一个学渣的阿里之路](https://crossoverjie.top/2018/06/21/personal/Interview-experience/)
- [如何成为一位「不那么差」的程序员](https://crossoverjie.top/2018/08/12/personal/how-to-be-developer/)
- [如何高效的使用 Git](https://github.com/crossoverJie/JCSprout/blob/master/MD/additional-skills/how-to-use-git-efficiently.md)


### 联系作者

> crossoverJie#gmail.com

![index.jpg](https://i.loli.net/2021/10/12/ckQW9LYXSxFogJZ.jpg) 
