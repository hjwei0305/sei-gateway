package com.ecmp.apigateway.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ecmp.apigateway.service.IGatewayApiAppService;

public class GatewayApiAppServiceImpl implements IGatewayApiAppService {
    public final static Logger logger = LoggerFactory.getLogger(GatewayApiAppServiceImpl.class);

    @Override
    public Object findAllApiApp() {
        logger.error("GatewayApiAppService HTTP Get is Error.");
        return null;
    }
}
