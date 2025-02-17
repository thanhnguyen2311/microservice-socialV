package com.example.gatewayserver.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator socialVRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path("/socialv/user-service/**")
                        .filters(f -> f.rewritePath("/socialv/user-service/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("userServiceCircuitBreaker")
                                        .setFallbackUri("forward:/default-error")))
                        .uri("lb://USER-SERVICE"))
                .route(p -> p
                        .path("/socialv/post-service/**")
                        .filters(f -> f.rewritePath("/socialv/post-service/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("postServiceCircuitBreaker")
                                        .setFallbackUri("forward:/default-error")))
                        .uri("lb://POST-SERVICE")).build();
    }
}
