package com.changhong.sei.apigateway.handler;

import com.changhong.sei.core.dto.ResultData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.cloud.gateway.actuate.GatewayControllerEndpoint;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("routes")
public class RoutesHandler {

    @Autowired(required = false)
    private GatewayControllerEndpoint gatewayControllerEndpoint;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired(required = false)
    private ServiceRegistry<Registration> serviceRegistry;

    @Autowired
    private ChannelTopic channelTopic;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 重新载入网关配置到缓存
     */
    @CrossOrigin
    @PostMapping("reloadCache")
    public ResultData<Boolean> reloadCache() {
        // 发布重载配置消息 @{SubscribeListener#onMessage}
        stringRedisTemplate.convertAndSend(channelTopic.getTopic(), "reloadConfigCache");
        return ResultData.success(Boolean.TRUE);
    }

    @GetMapping("getAllRoutes")
    public Flux<Map<String, Object>> getAllRoutes() {
        return gatewayControllerEndpoint.routes();
    }

    @PostMapping("refresh")
    public ResultData<?> refreshRoutes() {
        return ResultData.success(gatewayControllerEndpoint.refresh());
    }

    @GetMapping("getInstance")
    public ResultData<List<ServiceInstance>> getInstance(@RequestParam String serviceId) {
        List<ServiceInstance> data = discoveryClient.getInstances(serviceId);
        return ResultData.success(data);
    }

    @GetMapping("deleteInstance")
    public ResultData<?> deleteInstance(@RequestParam String serviceId, @RequestParam String instanceId) {
        List<ServiceInstance> list = discoveryClient.getInstances(serviceId);
        if (CollectionUtils.isNotEmpty(list)) {
            for (ServiceInstance instance : list) {
                if (StringUtils.equals(instanceId, instance.getInstanceId())) {
                    if (instance instanceof Registration) {
                        Registration registration = (Registration) instance;
                        serviceRegistry.deregister(registration);
                    }
                }
            }
        }

        return ResultData.success(null);
    }

}
