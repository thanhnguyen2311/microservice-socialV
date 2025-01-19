package com.example.postservice.component;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestFactory {
    private static final RestTemplate template = new RestTemplate();

    public static RestTemplate buildTemplate() {

        return template;
    }

    public static ResponseEntity<String> postUserService(String url, String fnc, Object requestData) {
        HttpEntity<Object> request = new HttpEntity<>(requestData);
        RestTemplate restTemplate = buildTemplate();
        return restTemplate.postForEntity(url + fnc, request, String.class);
    }
}
