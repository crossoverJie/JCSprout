# LinkedList 底层分析

![](https://ws1.sinaimg.cn/large/006tKfTcgy1fmtdndjiwej30hj06mabj.jpg)

如图所示 `LinkedList` 底层是基于双向链表实现的，也是实现了 `List` 接口，所以也拥有 List 的一些特点。

## 新增

```java
    public boolean add(E e) {
        linkLast(e);
        return true;
    }
     /**
     * Links e as last element.
     */
    void linkLast(E e) {
        final Node<E> l = last;
        final Node<E> newNode = new Node<>(l, e, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        size++;
        modCount++;
    }
```

可见每次插入都是移动指针，和 ArrayList 的拷贝数组来说效率要高上不少。

## 查询

```java
    public E get(int index) {
        checkElementIndex(index);
        return node(index).item;
    }
    
    Node<E> node(int index) {
        // assert isElementIndex(index);

        if (index < (size >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<E> x = last;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }
```

由此可以看出是使用二分查找来看 `index` 离 size 中间距离来判断是从头结点正序查还是从尾节点倒序查。

这样的效率是非常低的，特别是当 index 距离 size 的中间位置越远时。

总结：

- LinkedList 插入，删除都是移动指针效率很高。
- 查找需要进行遍历查询，效率较低。