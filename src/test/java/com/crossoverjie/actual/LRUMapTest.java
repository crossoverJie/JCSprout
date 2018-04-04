package com.crossoverjie.actual;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.bcel.internal.generic.LUSHR;
import org.junit.Test;

import static org.junit.Assert.*;

public class LRUMapTest {

    @Test
    public void put() throws Exception {
        LRUMap<String,Integer> lruMap = new LRUMap(3) ;
        lruMap.put("1",1) ;
        lruMap.put("2",2) ;
        lruMap.put("3",3) ;

        System.out.println(lruMap.toString());

        lruMap.put("4",4) ;
        System.out.println(lruMap.toString());

        lruMap.put("5",5) ;
        System.out.println(lruMap.toString());
    }

}