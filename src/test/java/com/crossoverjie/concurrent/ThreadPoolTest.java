package com.crossoverjie.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;


public class ThreadPoolTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(ThreadPoolTest.class);



    public static void main(String[] args) throws InterruptedException {
        BlockingQueue queue = new ArrayBlockingQueue<>(4);
        ExecutorService pool = new ThreadPoolExecutor(3,5,1, TimeUnit.SECONDS,queue,new ThreadPoolExecutor.DiscardOldestPolicy()) ;
        for (int i = 0; i < 10; i++) {
            pool.execute(new Worker(i));
            LOGGER.info("=======线程池活跃线程数={}======",((ThreadPoolExecutor) pool).getActiveCount());
        }

        TimeUnit.SECONDS.sleep(5);
        LOGGER.info("=======休眠后线程池活跃线程数={}======",((ThreadPoolExecutor) pool).getActiveCount());

        for (int i = 0; i < 3; i++) {
            pool.execute(new Worker(i + 100));
        }

        pool.shutdown();
        LOGGER.info("++++++++++++++");
    }




    private static class Worker implements Runnable{

        private int state ;

        public Worker(int state) {
            this.state = state;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(2);
                LOGGER.info("state={}",state);
            } catch (InterruptedException e) {

            }
        }
    }



}