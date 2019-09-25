package com.crossoverjie.guava;

import com.google.common.cache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 2018/6/12 15:33
 * @since JDK 1.8
 */
public class CacheLoaderTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(CacheLoaderTest.class);
    private LoadingCache<Integer, AtomicLong> loadingCache ;
    private final static Integer KEY = 1000;


    private final static LinkedBlockingQueue<Integer> QUEUE = new LinkedBlockingQueue<>(1000);


    private void init() throws InterruptedException {
        loadingCache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .removalListener(new RemovalListener<Object, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<Object, Object> notification) {
                        LOGGER.info("删除原因={}，删除 key={},删除 value={}",notification.getCause(),notification.getKey(),notification.getValue());
                    }
                })
                .build(new CacheLoader<Integer, AtomicLong>() {
                    @Override
                    public AtomicLong load(Integer key) throws Exception {
                        return new AtomicLong(0);
                    }
                });


        for (int i = 10; i < 15; i++) {
            QUEUE.put(i);
        }
    }

    private void checkAlert(Integer integer) {
        try {

            //loadingCache.put(integer,new AtomicLong(integer));

            TimeUnit.SECONDS.sleep(3);


            LOGGER.info("当前缓存值={},缓存大小={}", loadingCache.get(KEY),loadingCache.size());
            LOGGER.info("缓存的所有内容={}",loadingCache.asMap().toString());
            loadingCache.get(KEY).incrementAndGet();

        } catch (ExecutionException e ) {
            LOGGER.error("Exception", e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        CacheLoaderTest cacheLoaderTest = new CacheLoaderTest() ;
        cacheLoaderTest.init();



        while (true) {

            try {
                Integer integer = QUEUE.poll(200, TimeUnit.MILLISECONDS);
                if (null == integer) {
                    break;
                }
                //TimeUnit.SECONDS.sleep(5);
                cacheLoaderTest.checkAlert(integer);
                LOGGER.info("job running times={}", integer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
