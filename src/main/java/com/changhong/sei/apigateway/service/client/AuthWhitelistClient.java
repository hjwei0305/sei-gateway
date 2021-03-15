package com.changhong.sei.apigateway.service.client;

import com.changhong.sei.apigateway.service.client.dto.AuthWhitelistDto;
import com.changhong.sei.core.dto.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(url = "${sei.manager.url}", path = "authWhitelist")
public interface AuthWhitelistClient {

    /**
     * 获取指定环境的白名单
     *
     * @param envCode 环境代码
     * @return 业务实体
     */
    @GetMapping(path = "get")
    ResultData<List<AuthWhitelistDto>> get(@RequestParam("envCode") String envCode);

}