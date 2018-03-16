package com.crossoverjie.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * Function:响应中断
 *
 * @author crossoverJie
 *         Date: 16/03/2018 01:41
 * @since JDK 1.8
 */
public class StopThread implements Runnable {
    @Override
    public void run() {

        while ( !Thread.currentThread().isInterrupted()) {
            // 线程执行具体逻辑
            System.out.println(Thread.currentThread().getName() + "运行中。。");
        }

        System.out.println(Thread.currentThread().getName() + "退出。。");

    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new StopThread(), "thread A");
        thread.start();

        System.out.println("main 线程正在运行") ;

        TimeUnit.MILLISECONDS.sleep(10) ;
        thread.interrupt();
    }


}
