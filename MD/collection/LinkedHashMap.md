# LinkedHashMap 底层分析

众所周知 `HashMap` 是一个无序的 `Map`，因为每次根据 `key` 的 `hashcode` 映射到 `Entry` 数组上，所以遍历出来的顺序并不是写入的顺序。

因此 JDK 推出一个基于 HashMap 但具有顺序的 LinkedHashMap 来解决有排序需求的场景。

它的底层是继承于 `HashMap` 实现的，由一个双向链表所构成。

`LinkedHashMap` 的排序方式有两种：

- 根据写入顺序排序。
- 根据访问顺序排序。

其中根据访问顺序排序时，每次 `get` 都会将访问的值移动到链表末尾，这样重复操作就能的到一个按照访问顺序排序的链表。

## 数据结构

```java
	@Test
	public void test(){
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		map.put("1",1) ;
		map.put("2",2) ;
		map.put("3",3) ;
		map.put("4",4) ;
		map.put("5",5) ;
		System.out.println(map.toString());

	}
```

调试可以看到 `map` 的组成：

![](https://ws2.sinaimg.cn/large/006tKfTcly1fo6l9xp91lj319m0s4tgi.jpg)
