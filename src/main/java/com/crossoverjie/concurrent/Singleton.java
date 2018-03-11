package com.crossoverjie.concurrent;

/**
 * Function:单例模式-双重检查锁
 *
 * @author crossoverJie
 *         Date: 09/03/2018 01:14
 * @since JDK 1.8
 */
public class Singleton {

    private static volatile Singleton singleton;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    //防止指令重排
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
