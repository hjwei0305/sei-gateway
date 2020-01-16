package com.ecmp.apigateway.intergration;

import com.ecmp.apigateway.entity.ResultData;

import com.ecmp.apigateway.intergration.fallback.AuthFromAccountCenterFallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "authFormAccountCenter", url = "${auth.host}", path = "${auth.path}", fallbackFactory = AuthFromAccountCenterFallBackFactory.class)
public interface AuthFromAccountCenter {

    @PostMapping(value = "/auth/getAnonymousToken",consumes = "application/json")
    ResultData<String> getAnonymousToken();


    @PostMapping(value = "/auth/check",consumes = "application/json")
    ResultData<String> check(String sid);

}