package com.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

                // ACCOUNT SERVICE ROUTE
                .route("account-service", r -> r
                        .path("/api/accounts/**")
                        .uri("lb://ACCOUNTMICROSERVICE"))

                // TRANSACTION SERVICE ROUTE
                .route("transaction-service", r -> r
                        .path("/api/transactions/**")
                        .uri("lb://TRANSACTIONSMICROSERVICE"))

                //NOTIFICATION SERVICE
                .route("notification-service",r->r
                        .path("/api/notifications/**")
                        .uri("lb://NOTIFICATIONMICROSERVICE"))

                .build();
    }
}
