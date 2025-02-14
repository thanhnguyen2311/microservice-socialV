package com.example.gatewayserver.filter;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Component
public class FilterUtility {
    public static final String REQUEST_ID = "Request-ID";

    public String getRequestId(HttpHeaders requestHeaders) {
        if (requestHeaders.get(REQUEST_ID) != null) {
            List<String> requestHeaderList = requestHeaders.get(REQUEST_ID);
            return requestHeaderList.stream().findFirst().get();
        } else {
            return null;
        }
    }

    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
        return exchange.mutate().request(exchange.getRequest().mutate().header(name, value).build()).build();
    }

    public ServerWebExchange setRequestId(ServerWebExchange exchange, String requestId) {
        return this.setRequestHeader(exchange, REQUEST_ID, requestId);
    }
}
