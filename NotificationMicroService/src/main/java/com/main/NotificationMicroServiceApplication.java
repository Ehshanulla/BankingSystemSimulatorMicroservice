package com.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = "com")
@EnableDiscoveryClient
public class NotificationMicroServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(NotificationMicroServiceApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(NotificationMicroServiceApplication.class, args);
        log.info("Notification server is up");
	}

}
