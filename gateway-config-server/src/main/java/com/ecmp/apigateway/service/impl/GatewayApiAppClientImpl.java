package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.exception.RequestAccessedException;
import com.ecmp.apigateway.service.IGatewayApiAppClient;

/**
 * @author: hejun
 * @date: 2018/4/26
 * @remark: 配置中心接口-业务层实现熔断异常
 */
public class GatewayApiAppClientImpl implements IGatewayApiAppClient {

    @Override
    public Object findAllApiApp() {
        throw new RequestAccessedException();
    }

    @Override
    public Object findAppByAppId(String appId) {
        throw new RequestAccessedException();
    }
}
