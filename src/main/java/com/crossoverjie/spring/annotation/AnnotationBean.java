package com.crossoverjie.spring.annotation;

import com.crossoverjie.spring.SpringLifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Function:用注解的方法
 *
 * @author crossoverJie
 *         Date: 20/03/2018 18:46
 * @since JDK 1.8
 */
@Component
public class AnnotationBean {
    private final static Logger LOGGER = LoggerFactory.getLogger(AnnotationBean.class);

    @PostConstruct
    public void start(){
        LOGGER.info("AnnotationBean start");
    }

    @PreDestroy
    public void destroy(){
        LOGGER.info("AnnotationBean destroy");
    }
}
