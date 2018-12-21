package com.crossoverjie.design.pattern.chainofresponsibility;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 2018/10/21 23:06
 * @since JDK 1.8
 */
public interface Process {

    /**
     * 执行处理
     * @param msg
     */
    void doProcess(String msg) ;
}
