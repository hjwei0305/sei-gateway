package com.ecmp.apigateway.service;

import com.ecmp.apigateway.service.impl.GatewayApiAppClientImpl;
import com.ecmp.apigateway.utils.Constants;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = Constants.configName, url = Constants.configUrl, fallback = GatewayApiAppClientImpl.class)
public interface IGatewayApiAppClient {

    @GetMapping(value = Constants.configPath, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Object findAllApiApp();

}
