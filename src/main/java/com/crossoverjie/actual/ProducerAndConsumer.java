package com.crossoverjie.actual;

/**
 * wait()和notify()方法的实现
 * 缓冲区满和为空时都调用wait()方法等待，当生产者生产了一个产品或者消费者消费了一个产品之后会唤醒所有线程。
 */
public class ProducerAndConsumer {

    private static final String Lock = "Lock";

    private static Integer count = 0;
    private static Integer FULL = 10;


    public static void main(String[] args) {

        ProducerAndConsumer test = new ProducerAndConsumer();

        new Thread(test.new Producer()).start();
        new Thread(test.new Producer()).start();
        new Thread(test.new Producer()).start();
        new Thread(test.new Producer()).start();
        new Thread(test.new Consumer()).start();


    }




    class Producer implements Runnable {

        @Override
        public void run() {
            for(int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (Lock) {
                    while (count == FULL) {
                        try {
                            Lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    count ++;
                    System.out.println(Thread.currentThread().getName() + "生产者生产，目前总共有：" + count);
                    Lock.notifyAll();
                }
            }
        }
    }


    class Consumer implements Runnable {
        @Override
        public void run() {
            for(int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (Lock) {
                    while (count == 0) {
                        try {
                            Lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    count --;
                    System.out.println(Thread.currentThread().getName() + "消费者消费，目前总共有：" + count);
                    Lock.notifyAll();
                }

            }
        }
    }

}
