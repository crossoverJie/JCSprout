package com.crossoverjie.spring;

import com.crossoverjie.concurrent.Singleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Function:使用 initMethod 和 destroyMethod 的方式
 *
 * @author crossoverJie
 *         Date: 19/03/2018 22:37
 * @since JDK 1.8
 */
@Configuration
public class LifeCycleConfig {


    @Bean(initMethod = "start", destroyMethod = "destroy")
    public SpringLifeCycle create(){
        SpringLifeCycle springLifeCycle = new SpringLifeCycle() ;

        return springLifeCycle ;
    }
}
