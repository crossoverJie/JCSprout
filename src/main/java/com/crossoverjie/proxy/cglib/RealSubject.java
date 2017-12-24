package com.crossoverjie.proxy.cglib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 24/12/2017 19:01
 * @since JDK 1.8
 */
public class RealSubject {
    private final static Logger LOGGER = LoggerFactory.getLogger(RealSubject.class);

    public void exec(){
        LOGGER.info("real exec");
    }
}
