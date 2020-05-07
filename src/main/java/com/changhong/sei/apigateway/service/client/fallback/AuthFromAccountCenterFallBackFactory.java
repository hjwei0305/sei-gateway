package com.changhong.sei.apigateway.service.client.fallback;

import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthFromAccountCenterFallBackFactory implements FallbackFactory<AuthFromAccountCenterFallBack> {

    private static final Logger log = LoggerFactory.getLogger(AuthFromAccountCenterFallBackFactory.class);

    @Autowired
    private AuthFromAccountCenterFallBack authFromAccountCenterFallBack;

    @Override
    public AuthFromAccountCenterFallBack create(Throwable throwable) {
        log.error("请求 auth 接口出错", throwable);
        return authFromAccountCenterFallBack;
    }


}