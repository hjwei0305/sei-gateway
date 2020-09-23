package com.changhong.sei.apigateway.service.client;

import com.changhong.sei.apigateway.service.client.fallback.AuthFromAccountCenterFallBackFactory;
import com.changhong.sei.core.dto.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "sei-auth", path = "auth", fallbackFactory = AuthFromAccountCenterFallBackFactory.class)
public interface AuthServiceClient {
    String INTERNAL_ERROR = "internal_error";

    @GetMapping(value = "/getAnonymousToken", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultData<String> getAnonymousToken();


    @PostMapping(value = "/check", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResultData<String> check(String sid);

}