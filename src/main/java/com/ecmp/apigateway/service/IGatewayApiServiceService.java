package com.ecmp.apigateway.service;

import com.ecmp.apigateway.model.GatewayApiService;

/**
 * @author: hejun
 * @date: 2018/4/25
 * @remark: 网关-应用服务-业务层接口
 */
public interface IGatewayApiServiceService {

    GatewayApiService save(GatewayApiService gatewayApiService);

    int edit(GatewayApiService gatewayApiService);

    int removeAll();

    int removeById(String ids);

    Object findAll(String keywords);

    Object findById(String id);

}
