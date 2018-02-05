# LinkedHashMap 底层分析

众所周知 `HashMap` 是一个无序的 `Map`，因为每次根据 `key` 的 `hashcode` 映射到 `Entry` 数组上，所以遍历出来的顺序并不是写入的顺序。

因此 JDK 推出一个基于 HashMap 但具有顺序的 LinkedHashMap 来解决有排序需求的场景。

它的底层是继承于 `HashMap` 实现的，由一个双向链表所构成。

LinkedHashMap 的排序方式有两种：

- 根据写入顺序排序。
- 根据访问顺序排序。

其中根据访问顺序排序时，每次 get 都会将访问的值移动到链表末尾，这样重复操作就能的到一个按照访问顺序排序的链表。