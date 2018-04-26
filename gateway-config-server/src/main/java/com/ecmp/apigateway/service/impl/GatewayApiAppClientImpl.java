package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.service.IGatewayApiAppClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GatewayApiAppClientImpl implements IGatewayApiAppClient {
    public final static Logger logger = LoggerFactory.getLogger(GatewayApiAppClientImpl.class);

    @Override
    public Object findAllApiApp() {
        logger.error("GatewayApiAppClient Requesting Http Error.");
        return null;
    }
}
