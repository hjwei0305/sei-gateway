package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.exception.RequestAccessedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ecmp.apigateway.service.IGatewayApiAppClient;

/**
 * @author: hejun
 * @date: 2018/4/26
 * @remark: 配置中心接口-业务层实现
 */
public class GatewayApiAppClientImpl implements IGatewayApiAppClient {
    public final static Logger logger = LoggerFactory.getLogger(GatewayApiAppClientImpl.class);

    @Override
    public Object findAllApiApp() {
        logger.error("Failed By findAllApiApp Requesting Http Error.");
        try {
            throw new RequestAccessedException();
        } finally {
            return null;
        }
    }

    @Override
    public Object findAppByAppId(String appId) {
        logger.error("Failed By findAppByAppId Requesting Http Error.");
        try {
            throw new RequestAccessedException();
        } finally {
            return null;
        }
    }
}
