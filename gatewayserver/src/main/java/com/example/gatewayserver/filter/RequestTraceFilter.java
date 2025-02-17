package com.example.gatewayserver.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(1)
@Component
@Slf4j
public class RequestTraceFilter implements GlobalFilter {

    @Autowired
    FilterUtility filterUtility;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        if (isRequestIdPresent(requestHeaders)) {
            log.info("Request-ID found in RequestTraceFilter : {}",
                    filterUtility.getRequestId(requestHeaders));
        } else {
            String requestId = generateRequestId();
            exchange = filterUtility.setRequestId(exchange, requestId);
            log.info("Request-ID generated in RequestTraceFilter : {}", requestId);
        }
        return chain.filter(exchange);
    }

    private boolean isRequestIdPresent(HttpHeaders requestHeaders) {
        if (filterUtility.getRequestId(requestHeaders) != null) {
            return true;
        } else {
            return false;
        }
    }

    private String generateRequestId() {
        return java.util.UUID.randomUUID().toString();
    }

}