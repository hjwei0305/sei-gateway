package com.changhong.sei.apigateway.config;

import com.changhong.sei.apigateway.service.InterfaceService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InterfaceInitToRedis implements InitializingBean {

    @Autowired
    private InterfaceService interfaceService;

    @Override
    public void afterPropertiesSet() throws Exception {
        interfaceService.reloadCache();
    }
}
