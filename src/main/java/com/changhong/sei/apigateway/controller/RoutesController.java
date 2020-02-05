package com.changhong.sei.apigateway.controller;

import com.changhong.sei.core.dto.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.actuate.GatewayControllerEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("routes")
public class RoutesController {

    @Autowired
    private GatewayControllerEndpoint gatewayControllerEndpoint;

    @GetMapping("getAllRoutes")
    public Mono<List<Map<String, Object>>> getAllRoutes(){
        return gatewayControllerEndpoint.routes();
    }

    @PostMapping("refresh")
    public ResultData<?> refreshRoutes(){
        return ResultData.success(gatewayControllerEndpoint.refresh());
    }
}
