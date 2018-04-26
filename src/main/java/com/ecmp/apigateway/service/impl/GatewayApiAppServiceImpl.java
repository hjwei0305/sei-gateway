package com.ecmp.apigateway.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ecmp.apigateway.service.IGatewayApiAppService;
import org.springframework.stereotype.Component;

@Component
public class GatewayApiAppServiceImpl implements IGatewayApiAppService {
    public final static Logger logger = LoggerFactory.getLogger(GatewayApiAppServiceImpl.class);

    @Override
    public String findAllApiApp() {
        logger.error("GatewayApiAppService HTTP Get is Error.");
        return null;
    }
}
