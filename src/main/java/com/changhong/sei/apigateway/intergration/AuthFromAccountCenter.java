package com.changhong.sei.apigateway.intergration;

import com.changhong.sei.apigateway.intergration.fallback.AuthFromAccountCenterFallBackFactory;

import com.changhong.sei.core.dto.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "authFormAccountCenter", url = "${auth.host}", path = "${auth.path}", fallbackFactory = AuthFromAccountCenterFallBackFactory.class)
public interface AuthFromAccountCenter {

    @GetMapping(value = "/auth/getAnonymousToken",consumes = "application/json")
    ResultData<String> getAnonymousToken();


    @PostMapping(value = "/auth/check",consumes = "application/json")
    ResultData<String> check(String sid);

}