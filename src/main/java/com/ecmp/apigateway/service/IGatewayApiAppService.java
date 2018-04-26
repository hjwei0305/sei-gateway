package com.ecmp.apigateway.service;

import com.ecmp.apigateway.service.impl.GatewayApiAppServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "ApplicationServiceService", url = "http://10.4.68.77:9089", fallback = GatewayApiAppServiceImpl.class)
public interface IGatewayApiAppService {

    @GetMapping(value = "/config-center-service/api/applicationService/findAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String findAllApiApp();

}
