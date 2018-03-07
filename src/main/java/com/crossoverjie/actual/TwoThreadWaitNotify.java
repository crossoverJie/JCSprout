package com.crossoverjie.actual;

/**
 * Function:两个线程交替执行打印 1~100
 * 等待通知机制版
 *
 * @author crossoverJie
 *         Date: 07/03/2018 13:19
 * @since JDK 1.8
 */
public class TwoThreadWaitNotify {

    private int start = 1;

    private boolean flag = false;

    public static void main(String[] args) {
        TwoThreadWaitNotify twoThread = new TwoThreadWaitNotify();

        Thread t1 = new Thread(new OuNum(twoThread));
        t1.setName("t1");


        Thread t2 = new Thread(new JiNum(twoThread));
        t2.setName("t2");

        t1.start();
        t2.start();
    }

    /**
     * 偶数线程
     */
    public static class OuNum implements Runnable {
        private TwoThreadWaitNotify number;

        public OuNum(TwoThreadWaitNotify number) {
            this.number = number;
        }

        @Override
        public void run() {

            while (number.start <= 100) {
                synchronized (TwoThreadWaitNotify.class) {
                    System.out.println("偶数线程抢到锁了");
                    if (number.flag) {
                        System.out.println(Thread.currentThread().getName() + "+-+偶数" + number.start);
                        number.start++;

                        number.flag = false;
                        TwoThreadWaitNotify.class.notify();

                    }else {
                        try {
                            TwoThreadWaitNotify.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
    }


    /**
     * 奇数线程
     */
    public static class JiNum implements Runnable {
        private TwoThreadWaitNotify number;

        public JiNum(TwoThreadWaitNotify number) {
            this.number = number;
        }

        @Override
        public void run() {
            while (number.start <= 100) {
                synchronized (TwoThreadWaitNotify.class) {
                    System.out.println("奇数线程抢到锁了");
                    if (!number.flag) {
                        System.out.println(Thread.currentThread().getName() + "+-+奇数" + number.start);
                        number.start++;

                        number.flag = true;

                        TwoThreadWaitNotify.class.notify();
                    }else {
                        try {
                            TwoThreadWaitNotify.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
