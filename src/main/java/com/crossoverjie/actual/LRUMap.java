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

    private int nodeCount ;


    private Node<K,V> header ;
    private Node<K,V> tailer ;


    public void put(K key, V value) {
        cacheMap.put(key, value);


    }

    private void addNode(K key,V value){
        //容量满了删除最后一个
        if (queueSize == nodeCount){
            tailer.next.tail = null ;
            tailer.next = null ;
            nodeCount -- ;

            //写入表头
            Node<K,V> node = new Node<>(key,value) ;
            header.next = node ;
        }else {

        }


    }


    private class Node<K,V> {
        private K key ;
        private V value ;
        Node<K,V> tail ;
        Node<K,V> next ;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
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
}
