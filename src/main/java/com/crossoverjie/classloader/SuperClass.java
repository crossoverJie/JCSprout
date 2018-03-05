package com.crossoverjie.classloader;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 05/03/2018 23:11
 * @since JDK 1.8
 */
public class SuperClass {

    /**
     * 如果使用了 final 修饰的常量，再使用时父类也不会初始化
     */
    public static int A = 1;

    static {
        System.out.println("SuperClass init");
    }
}
