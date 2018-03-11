# HashSet 底层分析

`HashSet` 是一个不允许存储重复元素的集合，它的实现比较简单，只要理解了 `HashMap`，`HashSet` 就水到渠成了。

## 成员变量
首先了解下 `HashSet` 的成员变量:

```java
    private transient HashMap<E,Object> map;

    // Dummy value to associate with an Object in the backing Map
    private static final Object PRESENT = new Object();
```

发现主要就两个变量:

- `map` ：用于存放最终数据的。
- `PRESENT` ：是所有写入 map 的 `value` 值。

## 构造函数

## add

## get

