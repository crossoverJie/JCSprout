package com.crossoverjie.basic;

import java.lang.reflect.Field;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 08/03/2018 13:56
 * @since JDK 1.8
 */
public class StringTest {

    public static void main(String[] args) throws NoSuchFieldException {
        String a = "123";
        System.out.println("a=" + a);

        a = "456";
        System.out.println("a=" + a);

        Field value = a.getClass().getField("value");
    }
}
