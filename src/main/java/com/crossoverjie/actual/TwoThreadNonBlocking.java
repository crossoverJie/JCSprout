package com.crossoverjie.actual;

/**
 * Function: 两个线程交替执行打印 1~100
 * <p>
 * non blocking 版
 *
 * @author twoyao
 * Date: 05/07/2018
 * @since JDK 1.8
 */

public class TwoThreadNonBlocking implements Runnable {

    /**
     * 当flag为1时只有奇数线程可以执行，并将其置为0
     * 当flag为0时只有偶数线程可以执行，并将其置为1
     */
    private volatile static int flag = 1;

    private int start;
    private int end;
    private String name;

    private TwoThreadNonBlocking(int start, int end, String name) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        while (start <= end) {
            int f = flag;
            if ((start & 0x01) == f) {
                System.out.println(name + "+-+" + start);
                start += 2;
                // 因为只可能同时存在一个线程修改该值，所以不会存在竞争
                flag ^= 0x1;
            }
        }
    }


    public static void main(String[] args) {
        new Thread(new TwoThreadNonBlocking(1, 100, "t1")).start();
        new Thread(new TwoThreadNonBlocking(2, 100, "t2")).start();
    }
}