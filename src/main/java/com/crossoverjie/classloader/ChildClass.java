package com.crossoverjie.classloader;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 05/03/2018 23:11
 * @since JDK 1.8
 */
public class ChildClass extends SuperClass {
    static {
        System.out.println("ChildClass init");
    }
}
