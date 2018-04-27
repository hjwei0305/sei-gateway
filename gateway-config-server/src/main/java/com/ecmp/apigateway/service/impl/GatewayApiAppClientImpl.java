package com.ecmp.apigateway.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ecmp.apigateway.service.IGatewayApiAppClient;

public class GatewayApiAppClientImpl implements IGatewayApiAppClient {
    public final static Logger logger = LoggerFactory.getLogger(GatewayApiAppClientImpl.class);

    @Override
    public Object findAllApiApp() {
        logger.error("GatewayApiAppClient Failed By findAllApiApp Requesting Http Error.");
        return null;
    }

    @Override
    public Object findAppByAppId(String appId) {
        logger.error("GatewayApiAppClient Failed By findAppByAppId Requesting Http Error.");
        return null;
    }
}
