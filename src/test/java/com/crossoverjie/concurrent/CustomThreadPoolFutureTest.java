package com.crossoverjie.concurrent;

import com.crossoverjie.concurrent.communication.Notify;
import com.crossoverjie.concurrent.future.Callable;
import com.crossoverjie.concurrent.future.Future;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class CustomThreadPoolFutureTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomThreadPoolFutureTest.class);
    @Test
    public void execute() {
    }


    public static void main(String[] args) throws InterruptedException {
        BlockingQueue queue = new ArrayBlockingQueue<>(10);
        CustomThreadPool pool = new CustomThreadPool(3, 5, 1, TimeUnit.SECONDS, queue, new Notify() {
            @Override
            public void notifyListen() {
                LOGGER.info("任务执行完毕");
            }
        }) ;

        List<Future> futures = new ArrayList<>() ;
        for (int i = 0; i < 10; i++) {
            Future<Integer> future = pool.submit(new Worker(i));
            futures.add(future) ;
        }

        pool.shutdown();
        LOGGER.info("++++++++++++++");
        pool.mainNotify();
        for (Future<Integer> future : futures) {
            Integer integer = future.get();
            LOGGER.info("future======{}" ,integer);
        }




    }




    private static class Worker implements Callable<Integer> {

        private int state ;

        public Worker(int state) {
            this.state = state;
        }

        @Override
        public Integer call() {
            try {
                TimeUnit.SECONDS.sleep(1);
                LOGGER.info("state={}",state);
                return state + 1 ;
            } catch (InterruptedException e) {

            }

            return 0 ;
        }
    }
}