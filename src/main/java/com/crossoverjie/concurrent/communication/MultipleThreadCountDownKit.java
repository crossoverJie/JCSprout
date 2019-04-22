package com.crossoverjie.concurrent.communication;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Function:
 *
 * @author crossoverJie
 * Date: 2019-04-17 19:35
 * @since JDK 1.8
 */
public final class MultipleThreadCountDownKit {

    private AtomicInteger count ;

    private Object notify ;

    private Notify notifyListen ;

    public MultipleThreadCountDownKit(int number){
        if (number < 0) {
            throw new IllegalArgumentException("count < 0");
        }
        count = new AtomicInteger(number) ;
        notify = new Object() ;
    }

    /**
     * 设置回调接口
     * @param notify
     */
    public void setNotify(Notify notify){
        notifyListen = notify ;
    }


    /**
     * 线程完成后计数 -1
     */
    public void countDown(){
        int count = this.count.decrementAndGet();
        if (count < 0){
            throw new RuntimeException("concurrent error") ;
        }

        if (count == 0){
            synchronized (notify){
                notify.notify();
            }
        }

    }

    /**
     * 等待所有的线程完成
     * @throws InterruptedException
     */
    public void await() throws InterruptedException {
        synchronized (notify){
            while (count.get() > 0){
                notify.wait();
            }

            if (notifyListen != null){
                notifyListen.notifyListen();
            }

        }
    }

}
