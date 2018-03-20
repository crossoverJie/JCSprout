package com.crossoverjie.spring;

import com.crossoverjie.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Function:
 *
 * @author crossoverJie
 *         Date: 20/03/2018 18:23
 * @since JDK 1.8
 */
public class SpringLifeCycle{

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringLifeCycle.class);
    public void start(){
        LOGGER.info("SpringLifeCycle start");
    }


    public void destroy(){
        LOGGER.info("SpringLifeCycle destroy");
    }
}
