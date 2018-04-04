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


    private Node<K, V> header;
    private Node<K, V> tailer;

    public LRUMap(int queueSize) {
        this.queueSize = queueSize;
        header = new Node<>();
        header.next = null;

        tailer = new Node<>();
        tailer.tail = null;

        //双向链表
        header.tail = tailer;
        tailer.next = header;


    }

    public void put(K key, V value) {
        cacheMap.put(key, value);
        addNode(key, value);
    }

    private void addNode(K key, V value) {

        Node<K, V> node = new Node<>(key, value);

        //容量满了删除最后一个
        if (queueSize == nodeCount) {
            delTail();

            //写入表头
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

        header.next = node;
        node.tail = header;
        header = node;
        nodeCount++;

        if (nodeCount == 2) {
            tailer.next.next.tail = null;
            tailer = tailer.next.next;
        }

    }

    private void delTail() {
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
