package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.service.IGatewayApiRouterClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author: hejun
 * @date: 2018/4/26
 * @remark: 网关路由接口-远程调用实现-熔断回调
 */
@Component
public class GatewayApiRouterClientImpl implements FallbackFactory<IGatewayApiRouterClient> {

    @Override
    public IGatewayApiRouterClient create(Throwable cause) {
        return new IGatewayApiRouterClient() {
            @Override
            public Object refresh() {
                return null;
            }
        };
    }
}
