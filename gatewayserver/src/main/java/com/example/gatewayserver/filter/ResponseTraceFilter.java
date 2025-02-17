package com.example.gatewayserver.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class ResponseTraceFilter {

    @Autowired
    FilterUtility filterUtility;

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                String requestId = filterUtility.getRequestId(requestHeaders);
                log.info("Updated the Request-ID to the outbound headers: {}", requestId);
                exchange.getResponse().getHeaders().add(filterUtility.REQUEST_ID, requestId);
            }));
        };
    }
}