package com.crossoverjie.algorithm;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwoStackQueueTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(TwoStackQueueTest.class);
    @Test
    public void queue(){
        TwoStackQueue<String> twoStackQueue = new TwoStackQueue<String>() ;
        twoStackQueue.appendTail("1") ;
        twoStackQueue.appendTail("2") ;
        twoStackQueue.appendTail("3") ;
        twoStackQueue.appendTail("4") ;
        twoStackQueue.appendTail("5") ;


        int size = twoStackQueue.getSize();

        for (int i = 0; i< size ; i++){
            LOGGER.info(twoStackQueue.deleteHead());
        }

        LOGGER.info("========第二次添加=========");

        twoStackQueue.appendTail("6") ;

        size = twoStackQueue.getSize();

        for (int i = 0; i< size ; i++){
            LOGGER.info(twoStackQueue.deleteHead());
        }
    }

}