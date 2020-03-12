package com.crossoverjie.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


public class ThreadPoolTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(ThreadPoolTest.class);



    public static void main(String[] args) throws Exception {
        BlockingQueue queue = new ArrayBlockingQueue<>(10);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(3,5,1, TimeUnit.SECONDS,queue,new ThreadPoolExecutor.DiscardOldestPolicy()) ;

        List<Future> futures = new ArrayList<>() ;
        for (int i = 0; i < 10; i++) {
            Future<Integer> future = pool.submit(new Worker(i));
            futures.add(future) ;
        }

        pool.shutdown();

        for (Future future : futures) {
            LOGGER.info("执行结果={}",future.get());
        }
        LOGGER.info("++++++++++++++");
    }




    private static class Worker implements Callable<Integer>{

        private int state ;

        public Worker(int state) {
            this.state = state;
        }

        @Override
        public Integer call() {
            try {
                TimeUnit.SECONDS.sleep(2);
                LOGGER.info("state={}",state);
                return state ;
            } catch (InterruptedException e) {

            }

            return -1;
        }
    }



}