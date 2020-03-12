package com.crossoverjie.concurrent;

/**
 * Function: 数组实现的线程安全阻塞队列
 *
 * @author crossoverJie
 * Date: 2019-04-04 15:02
 * @since JDK 1.8
 */
public final class ArrayQueue<T> {

    /**
     * 队列数量
     */
    private int count = 0;

    /**
     * 最终的数据存储
     */
    private Object[] items;

    /**
     * 队列满时的阻塞锁
     */
    private Object full = new Object();

    /**
     * 队列空时的阻塞锁
     */
    private Object empty = new Object();


    /**
     * 写入数据时的下标
     */
    private int putIndex;

    /**
     * 获取数据时的下标
     */
    private int getIndex;

    public ArrayQueue(int size) {
        items = new Object[size];
    }

    /**
     * 从队列尾写入数据
     * @param t
     */
    public void put(T t) {

        synchronized (full) {
            while (count == items.length) {
                try {
                    full.wait();
                } catch (InterruptedException e) {
                    break;
                }
            }
        }

        synchronized (empty) {
            //写入
            items[putIndex] = t;
            count++;

            putIndex++;
            if (putIndex == items.length) {
                //超过数组长度后需要从头开始
                putIndex = 0;
            }

            empty.notify();
        }

    }

    /**
     * 从队列头获取数据
     * @return
     */
    public T get() {

        synchronized (empty) {
            while (count == 0) {
                try {
                    empty.wait();
                } catch (InterruptedException e) {
                    return null;
                }
            }
        }

        synchronized (full) {
            Object result = items[getIndex];
            items[getIndex] = null;
            count--;

            getIndex++;
            if (getIndex == items.length) {
                getIndex = 0;
            }

            full.notify();

            return (T) result;
        }
    }

    /**
     * 获取队列大小
     * @return
     */
    public synchronized int size() {
        return count;
    }


    /**
     * 判断队列是否为空
     * @return
     */
    public boolean isEmpty() {
        return size() == 0;
    }


}
