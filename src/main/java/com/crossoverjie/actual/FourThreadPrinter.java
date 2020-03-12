package com.crossoverjie.actual;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 2018/11/15 18:06
 * @since JDK 1.8
 */
public class FourThreadPrinter extends Thread {

    private final static Logger LOGGER = LoggerFactory.getLogger(FourThreadPrinter.class);

    private static int count = 100;

    private static Lock lock = new ReentrantLock();

    private static volatile int index = 0;

    private static volatile boolean flag = false;

    /**
     * t1=1
     * t2=2
     * t3=3
     * t4=4
     */
    private static volatile int type = 1;

    /**
     * 线程当前状态
     */
    private int currentType;

    public FourThreadPrinter(String name, int currentType) {
        super(name);
        this.currentType = currentType;
    }


    @Override
    public void run() {
        while (index < count) {
            if (currentType == type) {
                try {
                    lock.lock();
                    index++;
                    LOGGER.info("print: " + index + " flag=" + flag);
                    updateCondition();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private void updateCondition() {

        if (Thread.currentThread().getName().equals("t1")) {
            type = 2;
        } else if (Thread.currentThread().getName().equals("t2")) {
            type = 3;
        } else if (Thread.currentThread().getName().equals("t3")) {
            type = 4;
        } else if (Thread.currentThread().getName().equals("t4")) {
            type = 1;
        }

    }

    public static void main(String[] args) {
        Thread t1 = new FourThreadPrinter("t1", 1);
        Thread t2 = new FourThreadPrinter("t2", 2);
        Thread t3 = new FourThreadPrinter("t3", 3);
        Thread t4 = new FourThreadPrinter("t4", 4);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}