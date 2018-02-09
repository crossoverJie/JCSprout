package com.crossoverjie.algorithm;

import java.util.Stack;

/**
 * Function: 两个栈实现队列
 *
 * @author crossoverJie
 *         Date: 09/02/2018 23:51
 * @since JDK 1.8
 */
public class TwoStackQueue {

    private Stack input = new Stack() ;
    private Stack out = new Stack() ;


    /**
     * 写入队列
     * @param object
     */
    private void appendTail(Object object){
        input.push(object) ;
    }


}
