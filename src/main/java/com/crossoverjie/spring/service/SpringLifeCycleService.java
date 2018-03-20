package com.crossoverjie.spring.service;

import com.crossoverjie.spring.SpringLifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Function:实现 InitializingBean DisposableBean 接口
 *
 * @author crossoverJie
 *         Date: 20/03/2018 18:38
 * @since JDK 1.8
 */
@Service
public class SpringLifeCycleService implements InitializingBean,DisposableBean{
    private final static Logger LOGGER = LoggerFactory.getLogger(SpringLifeCycleService.class);
    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("SpringLifeCycleService start");
    }

    @Override
    public void destroy() throws Exception {
        LOGGER.info("SpringLifeCycleService destroy");
    }
}
