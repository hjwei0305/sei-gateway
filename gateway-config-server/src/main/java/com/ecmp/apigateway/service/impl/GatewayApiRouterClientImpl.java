package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.exception.RequestAccessedException;
import com.ecmp.apigateway.service.IGatewayApiRouterClient;

/**
 * @author: hejun
 * @date: 2018/4/26
 * @remark: 网关路由接口-业务层实现熔断异常
 */
public class GatewayApiRouterClientImpl implements IGatewayApiRouterClient {

    @Override
    public void refresh() {
        throw new RequestAccessedException();
    }
}
