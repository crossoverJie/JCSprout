**更多 HashMap 与 ConcurrentHashMap 相关请查看[这里](https://crossoverjie.top/2018/07/23/java-senior/ConcurrentHashMap/)。**

# HashMap 底层分析

> 以下基于 JDK1.7 分析。

![](https://ws2.sinaimg.cn/large/006tNc79gy1fn84b0ftj4j30eb0560sv.jpg)

如图所示，HashMap 底层是基于数组和链表实现的。其中有两个重要的参数：

- 容量
- 负载因子

容量的默认大小是 16，负载因子是 0.75，当 `HashMap` 的 `size > 16*0.75` 时就会发生扩容(容量和负载因子都可以自由调整)。

## put 方法
首先会将传入的 Key 做 `hash` 运算计算出 hashcode,然后根据数组长度取模计算出在数组中的 index 下标。

由于在计算中位运算比取模运算效率高的多，所以 HashMap 规定数组的长度为 `2^n` 。这样用 `2^n - 1` 做位运算与取模效果一致，并且效率还要高出许多。

由于数组的长度有限，所以难免会出现不同的 Key 通过运算得到的 index 相同，这种情况可以利用链表来解决，HashMap 会在 `table[index]`处形成链表，采用头插法将数据插入到链表中。

## get 方法

get 和 put 类似，也是将传入的 Key 计算出 index ，如果该位置上是一个链表就需要遍历整个链表，需要通过如下2类条件同时满足来找到对应元素：

1. 链表当前元素的key同传入的key的hashcode相同。
2. 链表当前元素的key内存地址同传入的key内存地址相同，即`e.key== key `；或者是 `equals`方法相同。

>这个也是为什么使用HashMap这类结构需要重写`equlas`和`hashcode`的原因。

其核心源码如下:

```java
if (e.hash == hash && //上述1
                ((k = e.key) == key || (key != null && key.equals(k)))) //上述2
                return e;
             }
```

## 遍历方式


```java
 Iterator<Map.Entry<String, Integer>> entryIterator = map.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, Integer> next = entryIterator.next();
            System.out.println("key=" + next.getKey() + " value=" + next.getValue());
        }
```

```java
Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            System.out.println("key=" + key + " value=" + map.get(key));

        }
```

```java
map.forEach((key,value)->{
    System.out.println("key=" + key + " value=" + value);
});
```

**强烈建议**使用第一种 EntrySet 进行遍历。

第一种可以把 key value 同时取出，第二种还得需要通过 key 取一次 value，效率较低, 第三种需要 `JDK1.8` 以上，通过外层遍历 table，内层遍历链表或红黑树。 


## JDK7死循环

在`JDK7`的并发环境下使用 `HashMap` 容易出现死循环。

并发场景发生扩容，调用 `resize()` 方法里的 `rehash()` 时，容易出现环形链表。

假设一个HashMap的初始容量是4，使用默认负载因子0.75，有三个元素通过Hash算法计算出的数组下标都是2，但是key值都不同，分别是a1、a2、a3，HashMap内部存储如下图：

![img](http://t11.baidu.com/it/u=131674705,1881035944&fm=173&app=25&f=JPEG?w=640&h=279&s=10B0ED378FA2480B58DD8CD302004031) 

假设插入的第四个元素a4，通过Hash算法计算出的数组下标也是2，当插入时则需要扩容，此时有两个线程T1、T2同时插入a4，则T1、T2同时进行扩容操作，它们各自新建了一个Entry数组newTable。

![img](http://t12.baidu.com/it/u=160780133,1856569706&fm=173&app=25&f=JPEG?w=640&h=455&s=10B4ED334B636D2A5A5DB1DA02005031)

T2线程执行到transfer方法的Entry next = e.next;时被挂起，T1线程执行transfer方法后Entry数组如下图：

![img](http://t12.baidu.com/it/u=1724205860,531282159&fm=173&app=25&f=JPEG?w=640&h=448&s=10B4ED374B8B414B1AF5E5D302008031)



在T1线程返回新建Entry数组复制给table之后，T2线程恢复，因为在T2挂起时，变量e指向的是a1，变量next指向的是a2，所以在T2恢复执行完transfer之后，Entry数组如下图：

![img](http://t11.baidu.com/it/u=640733404,1165409620&fm=173&app=25&f=JPEG?w=640&h=452&s=10B4ED33E3CF414B0AFDC5D302008031)

可以看到在T2执行完transfer方法后，a1元素和a2元素形成了循环引用，当调用get方法获取该位置的元素时就会发生死循环，更严重会导致CPU占用100%故障。

> 所以 HashMap 建议只在单线程中使用，并且尽量预设容量，尽可能的减少扩容。

# JDK8的改进

在 `JDK1.8` 中对 `HashMap` 进行了优化：

- 当 `hash` 碰撞之后写入链表的长度超过了阈值(默认为8)，链表将会转换为**红黑树**。
- 新的元素插入链表或者红黑树的时候，不同于`JDK7`采用的头插法，`JDK8`采用的是尾插法。
- 扩容的时候，JDK7会导致链表导致，JDK8会保持原来顺序，避免了并发下死循环的产生。

> 假设 `hash` 冲突非常严重，一个数组后面接了很长的链表，此时重新的时间复杂度就是 `O(n)` 。如果是红黑树，时间复杂度就是 `O(logn)` 。大大提高了查询效率。

多线程场景下推荐使用 [ConcurrentHashMap](https://github.com/crossoverJie/Java-Interview/blob/master/MD/ConcurrentHashMap.md)。
