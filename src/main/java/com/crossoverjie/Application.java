package com.crossoverjie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author crossoverJie
 *
 */
@SpringBootApplication
public class Application {

    private final static Logger LOGGER = LoggerFactory.getLogger(Application.class);


    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
        LOGGER.info("start ok!");

    }

}
