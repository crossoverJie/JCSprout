package com.crossoverjie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.system.ApplicationPidFileWriter;

/**
 * @author crossoverJie
 *
 */
@SpringBootApplication
public class Application {

    private final static Logger LOGGER = LoggerFactory.getLogger(Application.class);


    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(Application.class).listeners(new ApplicationPidFileWriter())
                .run(args);

    }

}
