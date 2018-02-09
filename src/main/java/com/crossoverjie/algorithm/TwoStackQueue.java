package com.crossoverjie.algorithm;

import java.util.Stack;

/**
 * Function: 两个栈实现队列
 *
 * @author crossoverJie
 *         Date: 09/02/2018 23:51
 * @since JDK 1.8
 */
public class TwoStackQueue<T> {

    /**
     * 写入的栈
     */
    private Stack<T> input = new Stack() ;

    /**
     * 移除队列所出的栈
     */
    private Stack<T> out = new Stack() ;


    /**
     * 写入队列
     * @param t
     */
    public void appendTail(T t){
        input.push(t) ;
    }

    /**
     * 删除队列头结点 并返回删除数据
     * @return
     */
    public T deleteHead(){

        //是空的 需要将 input 出栈写入 out
        if (out.isEmpty()){
            while (!input.isEmpty()){
                out.push(input.pop()) ;
            }
        }

        //不为空时直接移除出栈就表示移除了头结点
        return out.pop() ;
    }


    public int getSize(){
        return input.size() + out.size() ;
    }

}
