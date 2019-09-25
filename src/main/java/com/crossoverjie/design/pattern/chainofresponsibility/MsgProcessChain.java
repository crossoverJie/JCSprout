package com.crossoverjie.design.pattern.chainofresponsibility;

import java.util.ArrayList;
import java.util.List;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 2018/10/22 00:08
 * @since JDK 1.8
 */
public class MsgProcessChain {

    private List<Process> chains = new ArrayList<>() ;

    /**
     * 添加责任链
     * @param process
     * @return
     */
    public MsgProcessChain addChain(Process process){
        chains.add(process) ;
        return this ;
    }

    /**
     * 执行处理
     * @param msg
     */
    public void process(String msg){
        for (Process chain : chains) {
            chain.doProcess(msg);
        }
    }
}
