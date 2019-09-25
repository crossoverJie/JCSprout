package com.crossoverjie.concurrent;

import com.crossoverjie.concurrent.communication.MultipleThreadCountDownKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class MultipleThreadCountDownKitTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(MultipleThreadCountDownKitTest.class) ;


    public static void main(String[] args) throws InterruptedException {
        MultipleThreadCountDownKit multipleThreadKit = new MultipleThreadCountDownKit(3);
        multipleThreadKit.setNotify(() -> LOGGER.info("三个线程完成了任务"));

        Thread t1= new Thread(() -> {
            try {
                //TimeUnit.SECONDS.sleep(5);
                LOGGER.info("t1...");
                multipleThreadKit.countDown();
            } catch (Exception e) {
            }
        });
        Thread t2= new Thread(() -> {
            try {
                //TimeUnit.SECONDS.sleep(3);
                LOGGER.info("t2...");
                multipleThreadKit.countDown();
            } catch (Exception e) {
            }
        });
        Thread t3= new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                LOGGER.info("t3...");
                multipleThreadKit.countDown();
            } catch (Exception e) {
            }
        });

        t1.start();
        t2.start();
        t3.start();

        multipleThreadKit.await();
        LOGGER.info("======================");
    }

}