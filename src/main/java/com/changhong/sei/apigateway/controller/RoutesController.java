package com.changhong.sei.apigateway.controller;

import com.changhong.sei.core.dto.ResultData;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.actuate.GatewayControllerEndpoint;
import org.springframework.cloud.zookeeper.discovery.ZookeeperDiscoveryProperties;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("routes")
public class RoutesController {

    @Autowired(required = false)
    private GatewayControllerEndpoint gatewayControllerEndpoint;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired(required = false)
    private ZookeeperDiscoveryProperties zookeeperDiscoveryProperties;

    @Autowired(required = false)
    private CuratorFramework curatorFramework;

    @GetMapping("getAllRoutes")
    public Flux<Map<String, Object>> getAllRoutes(){
        return gatewayControllerEndpoint.routes();
    }

    @PostMapping("refresh")
    public ResultData<?> refreshRoutes(){
        return ResultData.success(gatewayControllerEndpoint.refresh());
    }


    @GetMapping("getInstance")
    public ResultData<List<ServiceInstance>> getInstance(@RequestParam String serviceId){
        List<ServiceInstance> data = discoveryClient.getInstances(serviceId);
        return ResultData.success(data);
    }

    @GetMapping("deleteInstance")
    public ResultData<?> deleteInstance(@RequestParam String serviceId,@RequestParam String instanceId){
        String path = zookeeperDiscoveryProperties.getRoot()+"/"+serviceId+"/"+instanceId;
        try {
            curatorFramework.delete().forPath(path);
        } catch (Exception e) {
            return ResultData.fail(e.getMessage());
        }
        return ResultData.success(null);
    }

}
