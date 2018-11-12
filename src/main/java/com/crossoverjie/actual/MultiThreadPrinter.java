package com.crossoverjie.actual;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Function:
 *
 * @author songjian (https://github.com/devpage)
 *         Date: 12/11/2018 10:04
 * @since JDK 1.5
 */
public class MultiThreadPrinter extends Thread{

    // execute finish condition
    protected static Integer count = 100;

    // shared
    protected static Lock lock = new ReentrantLock();
    protected static volatile Integer index = 0;
    protected static volatile boolean over = false;
    //
    protected static volatile boolean flag = false;

    // executor sharded boolean partition
    private boolean c;

    public MultiThreadPrinter(String name, boolean condition) {
        super(name);
        this.c = condition;
    }

    void updateCondition() {
        over = ++index >= count;
        flag = !flag;
    }
    @Override
    public void run() {
        while (index < count) {
            if (c == flag) { // 判断分片执行条件
                lock.lock(); // 在最后 index 等于 99的时刻 多个线程获得了锁,不信你看下一句
                System.out.println(Thread.currentThread().getName() +" 获得了锁: "+ index+", over: " + over);
                if (over) { lock.unlock();break; }  // 不要被'前辈经验'所骗, 一定要加上边界
                try {
                    updateCondition();
                    System.out.println(Thread.currentThread().getName() +" print: "+ index);
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new MultiThreadPrinter("t1", false);
        Thread t2 = new MultiThreadPrinter("t2", true);
        Thread t3 = new MultiThreadPrinter("t3", true);
        Thread t4 = new MultiThreadPrinter("t4", false);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
