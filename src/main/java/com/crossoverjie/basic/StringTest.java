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

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        String a = "123";
        //这里的 a 和 b  都是同一个对象，指向同一个字符串常量池对象。
        String b = "123" ;
        String c = new String("123") ;

        System.out.println("a=b:" + (a == b));
        System.out.println("a=c:" + (a == c));

        System.out.println("a=" + a);

        a = "456";
        System.out.println("a=" + a);


        //用反射的方式改变字符串的值
        Field value = a.getClass().getDeclaredField("value");
        //改变 value 的访问属性
        value.setAccessible(true) ;

        char[] values = (char[]) value.get(a);
        values[0] = '9' ;

        System.out.println(a);
    }
}
