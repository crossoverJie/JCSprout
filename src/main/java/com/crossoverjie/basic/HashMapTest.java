package com.crossoverjie.basic;

import java.security.Key;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 05/05/2018 12:42
 * @since JDK 1.8
 */
public class HashMapTest {
    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<>(16);
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        map.put("4", 4);

        Iterator<Map.Entry<String, Integer>> entryIterator = map.entrySet().iterator();
        while (entryIterator.hasNext()) {
            Map.Entry<String, Integer> next = entryIterator.next();
            System.out.println("key=" + next.getKey() + " value=" + next.getValue());
        }
        System.out.println("=============");

        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            System.out.println("key=" + key + " value=" + map.get(key));

        }

        System.out.println("=============");
        map.forEach((key, value) -> {
            System.out.println("key=" + key + " value=" + map.get(key));
        });
    }
}
