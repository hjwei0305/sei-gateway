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
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<String> gatewayKeys = redisTemplate.keys("Gateway:NoToken*");
        redisTemplate.delete(gatewayKeys);
        interfaceService.loadRuntimeData();
    }
}
