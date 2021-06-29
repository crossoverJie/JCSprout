package com.crossoverjie.actual;

/**
 * Function:两个线程交替执行打印 1~100
 * 等待通知机制版
 *
 * @author crossoverJie
 * Date: 07/03/2018 13:19
 * @since JDK 1.8
 */
public class TwoThreadWaitNotifySimple {

    private boolean flag = false;

    public static void main(String[] args) {
        TwoThreadWaitNotifySimple twoThread = new TwoThreadWaitNotifySimple();

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
        private TwoThreadWaitNotifySimple number;

        public OuNum(TwoThreadWaitNotifySimple number) {
            this.number = number;
        }

        @Override
        public void run() {
            for (int i = 0; i < 11; i++) {
                synchronized (TwoThreadWaitNotifySimple.class) {
                    if (number.flag) {
                        if (i % 2 == 0) {
                            System.out.println(Thread.currentThread().getName() + "+-+偶数" + i);

                            number.flag = false;
                            TwoThreadWaitNotifySimple.class.notify();
                        }

                    } else {
                        try {
                            TwoThreadWaitNotifySimple.class.wait();
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
        private TwoThreadWaitNotifySimple number;

        public JiNum(TwoThreadWaitNotifySimple number) {
            this.number = number;
        }

        @Override
        public void run() {
            for (int i = 0; i < 11; i++) {
                synchronized (TwoThreadWaitNotifySimple.class) {
                    if (!number.flag) {
                        if (i % 2 == 1) {
                            System.out.println(Thread.currentThread().getName() + "+-+奇数" + i);

                            number.flag = true;
                            TwoThreadWaitNotifySimple.class.notify();
                        }

                    } else {
                        try {
                            TwoThreadWaitNotifySimple.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
