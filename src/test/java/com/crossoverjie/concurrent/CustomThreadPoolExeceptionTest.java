package com.crossoverjie.concurrent;

import com.crossoverjie.concurrent.communication.Notify;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class CustomThreadPoolExeceptionTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomThreadPoolExeceptionTest.class);
    @Test
    public void execute() {
    }


    public static void main(String[] args) throws InterruptedException {
        BlockingQueue queue = new ArrayBlockingQueue<>(1);
        CustomThreadPool pool = new CustomThreadPool(1, 1, 1, TimeUnit.SECONDS, queue, new Notify() {
            @Override
            public void notifyListen() {
                LOGGER.info("任务执行完毕");
            }
        }) ;

        pool.execute(new Worker(0));
        LOGGER.info("++++++++++++++");
        pool.mainNotify();

    }




    private static class Worker implements Runnable {

        private int state ;

        public Worker(int state) {
            this.state = state;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(1);
                LOGGER.info("state={}",state);

                while (true){
                    state ++ ;

                    if (state == 1000){
                        throw new NullPointerException("NullPointerException");
                    }
                }

            } catch (InterruptedException e) {

            }
        }
    }
}