package com.crossoverjie.actual;

import com.sun.scenario.effect.impl.prism.PrImage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 03/04/2018 00:08
 * @since JDK 1.8
 */
public class LRUMap<K, V> {
    private final Map<K, V> cacheMap = new HashMap<>();

    private int queueSize;

    private int nodeCount;


    /**
     * 头结点
     */
    private Node<K, V> header;

    /**
     * 尾结点
     */
    private Node<K, V> tailer;

    public LRUMap(int queueSize) {
        this.queueSize = queueSize;
        //头结点的下一个结点为空
        header = new Node<>();
        header.next = null;

        //尾结点的上一个结点为空
        tailer = new Node<>();
        tailer.tail = null;

        //双向链表 头结点的上结点指向尾结点
        header.tail = tailer;

        //尾结点的下结点指向头结点
        tailer.next = header;


    }

    public void put(K key, V value) {
        cacheMap.put(key, value);

        //双向链表中添加结点
        addNode(key, value);
    }

    private void addNode(K key, V value) {

        Node<K, V> node = new Node<>(key, value);

        //容量满了删除最后一个
        if (queueSize == nodeCount) {
            //删除尾结点
            delTail();

            //写入头结点
            addHead(node);
        } else {
            addHead(node);

        }


    }


    /**
     * 添加头结点
     *
     * @param node
     */
    private void addHead(Node<K, V> node) {

        //写入头结点
        header.next = node;
        node.tail = header;
        header = node;
        nodeCount++;

        //如果写入的数据大于2个 就将初始化的头尾结点删除
        if (nodeCount == 2) {
            tailer.next.next.tail = null;
            tailer = tailer.next.next;
        }

    }

    private void delTail() {
        //把尾结点从缓存中删除
        cacheMap.remove(tailer.getKey());

        //删除尾结点
        tailer.next.tail = null;
        tailer = tailer.next;

        nodeCount--;

    }

    private class Node<K, V> {
        private K key;
        private V value;
        Node<K, V> tail;
        Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public Node() {
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

    }

    @Override
    public String toString() {
        return cacheMap.toString();
    }
}
