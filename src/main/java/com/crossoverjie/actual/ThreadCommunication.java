package com.crossoverjie.actual;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.concurrent.*;

/**
 * Function:三种方式线程通信
 *
 * @author crossoverJie
 *         Date: 04/01/2018 22:57
 * @since JDK 1.8
 */
public class ThreadCommunication {
    private final static Logger LOGGER = LoggerFactory.getLogger(ThreadCommunication.class);

    public static void main(String[] args) throws Exception {
        //join();
        //executorService();
        //countDownLatch();
        //piped();
        cyclicBarrier();
    }

    /**
     * CyclicBarrier
     *
     * @throws Exception
     */
    private static void cyclicBarrier() throws Exception {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3) ;

        new Thread(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("thread run");
                try {
                    cyclicBarrier.await() ;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                LOGGER.info("thread end do something");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("thread run");
                try {
                    cyclicBarrier.await() ;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                LOGGER.info("thread end do something");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("thread run");
                try {
                    Thread.sleep(5000);
                    cyclicBarrier.await() ;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                LOGGER.info("thread end do something");
            }
        }).start();

        LOGGER.info("main thread");
    }

    private static void countDownLatch() throws Exception {
        int thread = 3;
        long start = System.currentTimeMillis();
        final CountDownLatch countDown = new CountDownLatch(thread);
        for (int i = 0; i < thread; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LOGGER.info("thread run");
                    try {
                        Thread.sleep(2000);
                        countDown.countDown();

                        LOGGER.info("thread end");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        countDown.await();
        long stop = System.currentTimeMillis();
        LOGGER.info("main over total time={}", stop - start);
    }

    /**
     * 利用线程池的 awaitTermination 方法，每隔一秒钟检查线程池是否执行完毕
     *
     * @throws Exception
     */
    private static void executorService() throws Exception {
        BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(10);
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MILLISECONDS, queue);
        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("running");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("running2");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        poolExecutor.shutdown();
        while (!poolExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
            LOGGER.info("线程还在执行。。。");
        }
        LOGGER.info("main over");
    }


    /**
     * 采用 join 线程间通信
     *
     * @throws InterruptedException
     */
    private static void join() throws InterruptedException {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("running");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("running2");
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();

        //等待线程1终止
        t1.join();

        //等待线程2终止
        t2.join();

        LOGGER.info("main over");
    }

    public static void piped() throws IOException {
        //面向于字符 PipedInputStream 面向于字节
        PipedWriter writer = new PipedWriter();
        PipedReader reader = new PipedReader();

        //输入输出流建立连接
        writer.connect(reader);


        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("running");
                try {
                    for (int i = 0; i < 10; i++) {

                        writer.write(i+"");
                        Thread.sleep(10);
                    }
                } catch (Exception e) {

                } finally {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                LOGGER.info("running2");
                int msg = 0;
                try {
                    while ((msg = reader.read()) != -1) {
                        LOGGER.info("msg={}", (char) msg);
                    }

                } catch (Exception e) {

                }
            }
        });
        t1.start();
        t2.start();
    }
}
