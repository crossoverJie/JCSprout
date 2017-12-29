# OOM 分析

## Java 堆内存溢出

在 Java 堆中只要不断的创建对象，并且 `GC-Roots` 到对象之间存在引用链这样 `JVM` 就会不会回收对象。只要将`-Xms(最下堆)`,`-Xmx(最大堆)` 设置为一样禁止自动扩展堆内存。
当使用一个 `while(true)` 循环来不断创建对象就会发生 `OutOfMemory`，还可以使用 `-XX:+HeapDumpOutofMemoryErorr` 当发生 OOM 时会自动 dump 堆栈。

伪代码:

```java
public void main(String[] args){
    List<String> list = new ArrayList(10) ;
    while(true){
        list.add("1") ;
    }
}
```

当出现 OOM 时可以通过工具来分析 `GC-Roots` [引用链](https://github.com/crossoverJie/Java-Interview/blob/master/MD/GarbageCollection.md#%E5%8F%AF%E8%BE%BE%E6%80%A7%E5%88%86%E6%9E%90%E7%AE%97%E6%B3%95) ，查看对象和 `GC-Roots` 是如何进行关联的，是否存在对象的生命周期过长，或者是这些对象确实改存在的，那就要考虑将堆内存调大了。


## 方法区/运行时常量池溢出