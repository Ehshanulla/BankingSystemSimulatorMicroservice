package com.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "com")
@EnableMongoRepositories(basePackages = "com.repository")
@EnableDiscoveryClient
public class AccountMicroServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(AccountMicroServiceApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(AccountMicroServiceApplication.class, args);
        log.info("Starting account microservice");
	}

}
