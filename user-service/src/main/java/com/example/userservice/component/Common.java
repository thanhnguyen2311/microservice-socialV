package com.example.userservice.component;

import com.example.userservice.dto.BaseResponse;
import org.springframework.stereotype.Component;

@Component
public class Common {
    public BaseResponse<Object> getErrorResponse(BaseResponse baseResponse) {
        baseResponse.setCode("99");
        baseResponse.setMessage("something went wrong");
        return baseResponse;
    }
}
