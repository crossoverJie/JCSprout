package com.crossoverjie.actual;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractMapTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractMapTest.class);

    @Test
    public void test(){
        AbstractMap map = new AbstractMap() ;
        map.put(1,1) ;
        map.put(2,2) ;

        Object o = map.get(1);
        LOGGER.info("size={}",map.size());

        map.remove(1) ;
        LOGGER.info("size"+map.size());
    }

    public static void main(String[] args) {
        AbstractMap map = new AbstractMap() ;
        map.put(1,1) ;
        map.put(2,2) ;

        Object o = map.get(1);
        LOGGER.info("size={}",map.size());

        map.remove(1) ;
        map.remove(2) ;
        LOGGER.info("size"+map.size());
    }
}