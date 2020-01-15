package com.ecmp.apigateway.config;

import com.ecmp.apigateway.service.InterfaceService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class InterfaceInitToRedis implements InitializingBean {

    @Autowired
    private InterfaceService interfaceService;

    @Override
    public void afterPropertiesSet() throws Exception {
        interfaceService.reloadCache();
    }
}
