package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.exception.RequestAccessedException;
import com.ecmp.apigateway.service.IGatewayApiRouterClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: hejun
 * @date: 2018/4/26
 * @remark: 网关路由接口-业务层实现
 */
public class GatewayApiRouterClientImpl implements IGatewayApiRouterClient {
    public final static Logger logger = LoggerFactory.getLogger(GatewayApiRouterClientImpl.class);

    @Override
    public void refresh() {
        logger.error("Failed By refresh Requesting Http Error.");
        throw new RequestAccessedException();
    }
}
