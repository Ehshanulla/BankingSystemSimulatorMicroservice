package com.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {

    private static final Logger log = LogManager.getLogger(EurekaApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(EurekaApplication.class, args);
        log.info("Starting eureka server");
	}

}
