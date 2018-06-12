package com.crossoverjie.guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
    private LoadingCache<Long, AtomicLong> loadingCache ;
    private final static Long KEY = 1L;


    private final static LinkedBlockingQueue<Integer> QUEUE = new LinkedBlockingQueue<>(1000);


    private void init() throws InterruptedException {
        loadingCache = CacheBuilder.newBuilder()
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .build(new CacheLoader<Long, AtomicLong>() {
                    @Override
                    public AtomicLong load(Long key) throws Exception {
                        return new AtomicLong(0L);
                    }
                });


        for (int i = 0; i < 5; i++) {
            QUEUE.put(i);
        }
    }

    private void checkAlert() {
        try {


            TimeUnit.SECONDS.sleep(5);

            LOGGER.info("当前缓存值={}", loadingCache.get(KEY));
            loadingCache.get(KEY).incrementAndGet();

        } catch (ExecutionException |InterruptedException e ) {
            LOGGER.error("Exception", e);
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
                cacheLoaderTest.checkAlert();
                LOGGER.info("job running times={}", integer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
