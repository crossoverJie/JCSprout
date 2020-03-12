package com.crossoverjie.concurrent;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ArrayQueueTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(ArrayQueueTest.class) ;

    @Test
    public void test() throws InterruptedException {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue(3);

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("thread[" + Thread.currentThread().getName() + "]" + queue.take());
            } catch (Exception e) {
            }
        }).start();

        queue.put("123");
        queue.put("1234");
        queue.put("12345");
        queue.put("123456");
        queue.size();


    }

    @Test
    public void put() {
        ArrayQueue<String> queue = new ArrayQueue<>(3);
        queue.put("123");
        queue.put("1234");
        queue.put("12345");
        System.out.println(queue.size());


        while (!queue.isEmpty()) {
            System.out.println(queue.get());
        }

    }

    @Test
    public void put2() {
        final ArrayQueue<String> queue = new ArrayQueue<>(3);

        new Thread(() -> {
            try {
                LOGGER.info("[" + Thread.currentThread().getName() + "]" + queue.get());
            } catch (Exception e) {
            }
        }).start();


        queue.put("123");
        queue.put("1234");
        queue.put("12345");
        queue.put("123456");
        LOGGER.info("size=" + queue.size());


        while (!queue.isEmpty()) {
           LOGGER.info(queue.get());
        }

    }

    @Test
    public void put3() {
        final ArrayQueue<String> queue = new ArrayQueue<>(3);

        queue.put("123");
        queue.put("1234");
        queue.put("12345");
        queue.put("123456");
        System.out.println(queue.size());


        while (!queue.isEmpty()) {
            System.out.println(queue.get());
        }

    }

    @Test
    public void put4() throws InterruptedException {
        final ArrayQueue<String> queue = new ArrayQueue<>(299);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                queue.put(i + "");
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                queue.put(i + "");
            }
        });

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                queue.put(i + "");
            }
        });
        Thread t4 = new Thread(() -> {
            System.out.println("=====" + queue.get());
        });

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        System.out.println(queue.size());


    }

    @Test
    public void put5() throws InterruptedException {
        final ArrayQueue<String> queue = new ArrayQueue<>(1000000);

        long startTime = System.currentTimeMillis();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 500000; i++) {
                queue.put(i + "");
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 500000; i++) {
                queue.put(i + "");
            }
        });


        t1.start();
        t2.start();

        t1.join();
        t2.join();
        long end = System.currentTimeMillis();

        System.out.println("cast = [" + (end - startTime) + "]" + queue.size());

    }

    @Test
    public void put6() throws InterruptedException {
        final ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(1000000);

        long startTime = System.currentTimeMillis();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 500000; i++) {
                try {
                    queue.put(i + "");
                } catch (InterruptedException e) {
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 500000; i++) {
                try {
                    queue.put(i + "");
                } catch (InterruptedException e) {
                }
            }
        });


        t1.start();
        t2.start();

        t1.join();
        t2.join();
        long end = System.currentTimeMillis();

        System.out.println("cast = [" + (end - startTime) + "]" + queue.size());

    }


    @Test
    public void get2() throws InterruptedException {
        ArrayQueue<String> queue = new ArrayQueue<>(100);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                try {
                    queue.put(i + "");
                } catch (Exception e) {
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 50; i < 100; i++) {
                try {
                    queue.put(i + "");
                } catch (Exception e) {
                }
            }
        });

        Thread t3 = new Thread(() -> {
            System.out.println("开始消费");
            while (true) {
                System.out.println(queue.get());
            }
        });

        t3.start();
        t2.start();
        t1.start();

        t3.join();
        t2.join();
        t1.join();
    }

}