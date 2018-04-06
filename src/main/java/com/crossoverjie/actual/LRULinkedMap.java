package com.crossoverjie.actual;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 05/04/2018 12:04
 * @since JDK 1.8
 */
public class LRULinkedMap<K,V> {


    /**
     * 最大缓存大小
     */
    private int cacheSize;

    private LinkedHashMap<K,V> cacheMap ;


    public LRULinkedMap(int cacheSize) {
        this.cacheSize = cacheSize;

        cacheMap = new LinkedHashMap(16,0.75F,true){
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                if (cacheSize + 1 == cacheMap.size()){
                    return true ;
                }else {
                    return false ;
                }
            }
        };
    }

    public void put(K key,V value){
        cacheMap.put(key,value) ;
    }

    public V get(K key){
        return cacheMap.get(key) ;
    }


    public Collection<Map.Entry<K, V>> getAll() {
        return new ArrayList<Map.Entry<K, V>>(cacheMap.entrySet());
    }
}
