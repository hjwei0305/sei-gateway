package com.ecmp.apigateway.service;

import com.ecmp.apigateway.service.impl.GatewayApiAppServiceImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "ApplicationServiceService", fallback = GatewayApiAppServiceImpl.class)
public interface IGatewayApiAppService {

    @GetMapping(value = "applicationService/findAll")
    public Object findAllApiApp();

}
