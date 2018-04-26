package com.ecmp.apigateway.service;

import com.ecmp.apigateway.service.impl.GatewayApiAppClientImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "${config.center.appservice.name}", url = "${config.center.appservice.url}", fallback = GatewayApiAppClientImpl.class)
public interface IGatewayApiAppClient {

    @GetMapping(value = "${config.center.appservice.path}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Object findAllApiApp();

}
