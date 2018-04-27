package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.exception.RequestAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ecmp.apigateway.service.IGatewayApiAppClient;

/**
 * @author: hejun
 * @date: 2018/4/26
 * @remark: 访问接口-业务层实现
 */
public class GatewayApiAppClientImpl implements IGatewayApiAppClient {
    public final static Logger logger = LoggerFactory.getLogger(GatewayApiAppClientImpl.class);

    @Override
    public Object findAllApiApp() {
        logger.error("GatewayApiAppClient Failed By findAllApiApp Requesting Http Error.");
        throw new RequestAccessException();
        //return null;
    }

    @Override
    public Object findAppByAppId(String appId) {
        logger.error("GatewayApiAppClient Failed By findAppByAppId Requesting Http Error.");
        throw new RequestAccessException();
        //return null;
    }
}
