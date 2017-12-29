package com.crossoverjie.oom.heap;

import java.util.ArrayList;
import java.util.List;

/**
 * Function:堆内存溢出
 *
 * @author crossoverJie
 *         Date: 29/12/2017 18:22
 * @since JDK 1.8
 */
public class HeapOOM {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>(10) ;
        while (true){
            list.add("1") ;
        }
    }
}
