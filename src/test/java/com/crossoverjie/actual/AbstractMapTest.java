package com.crossoverjie.actual;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractMapTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractMapTest.class);

    @Test
    public void test(){
        LRUAbstractMap map = new LRUAbstractMap() ;
        map.put(1,1) ;
        map.put(2,2) ;

        Object o = map.get(1);
        LOGGER.info("getSize={}",map.size());

        map.remove(1) ;
        LOGGER.info("getSize"+map.size());
    }

    public static void main(String[] args) {
        LRUAbstractMap map = new LRUAbstractMap() ;
        map.put(1,1) ;
        map.put(2,2) ;

        Object o = map.get(1);
        LOGGER.info("getSize={}",map.size());

        map.remove(1) ;
        map.remove(2) ;
        LOGGER.info("getSize"+map.size());
    }
}