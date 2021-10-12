package com.crossoverjie.actual;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2021/7/1 23:08
 * @since JDK 11
 */
public class NotifyAll {


    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    synchronized (NotifyAll.class){
                        NotifyAll.class.wait();
                    }
                    System.out.println(Thread.currentThread().getName() + "done....");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        Thread.sleep(3000);
        synchronized (NotifyAll.class){
            NotifyAll.class.notifyAll();
        }


    }
}
