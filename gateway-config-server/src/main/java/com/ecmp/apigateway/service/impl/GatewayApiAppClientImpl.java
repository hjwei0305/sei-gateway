package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.service.IGatewayApiAppClient;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author: hejun
 * @date: 2018/4/26
 * @remark: 配置中心接口-远程调用实现-熔断回调
 */
@Component
public class GatewayApiAppClientImpl implements FallbackFactory<IGatewayApiAppClient> {

    @Override
    public IGatewayApiAppClient create(Throwable cause) {
        return new IGatewayApiAppClient() {
            @Override
            public Object findAllApiApp() {
                return null;
            }
        };
    }
}
