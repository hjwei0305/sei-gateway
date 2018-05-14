package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.exception.InvokeRouteFailException;
import com.ecmp.apigateway.service.IGatewayApiRouterClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author: hejun
 * @date: 2018/4/26
 * @remark: 网关路由接口-业务层实现-熔断异常
 */
@Component
public class GatewayApiRouterClientImpl implements FallbackFactory<IGatewayApiRouterClient> {

    @Override
    public IGatewayApiRouterClient create(Throwable cause) {
        return new IGatewayApiRouterClient() {
            @Override
            public void refresh() {
                throw new InvokeRouteFailException();
            }
        };
    }
}
