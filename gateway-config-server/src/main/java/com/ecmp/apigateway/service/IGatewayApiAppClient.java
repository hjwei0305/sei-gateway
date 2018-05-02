package com.ecmp.apigateway.service;

import com.ecmp.apigateway.service.impl.GatewayApiAppClientImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: hejun
 * @date: 2018/4/26
 * @remark: 配置中心接口-业务层接口
 */
@FeignClient(name = "${config.center.appservice.name}", url = "${config.center.appservice.url}", decode404 = true, fallback = GatewayApiAppClientImpl.class)
public interface IGatewayApiAppClient {

    /**
     * 获取配置中心应用服务
     * @return
     */
    @GetMapping(value = "${config.center.appservice.path0}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Object findAllApiApp();

    /**
     * 根据应用ID获取应用服务信息
     * @param appId 配置中心AppId
     * @return
     */
    @GetMapping(value = "${config.center.appservice.path1}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Object findAppByAppId(String appId);
}
