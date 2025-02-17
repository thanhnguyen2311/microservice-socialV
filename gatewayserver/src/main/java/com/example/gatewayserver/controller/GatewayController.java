package com.example.gatewayserver.controller;

import com.example.gatewayserver.response.ErrorResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayController {

    @RequestMapping("/default-error")
    public ErrorResponse getDefaultErrorResponse() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode("99");
        errorResponse.setMessage("disconnect to server");
        return errorResponse;
    }
}
