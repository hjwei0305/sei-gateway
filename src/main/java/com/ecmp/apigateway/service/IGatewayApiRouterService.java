package com.ecmp.apigateway.service;

import com.ecmp.apigateway.model.GatewayApiRouter;

/**
 * @author: hejun
 * @date: 2018/4/25
 * @remark: 网关-路由配置-业务层接口
 */
public interface IGatewayApiRouterService {

    GatewayApiRouter save(GatewayApiRouter gatewayApiRouter);

    int edit(GatewayApiRouter gatewayApiRouter);

    int removeAll();

    int removeById(String ids);

    Object findAll(String keywords);

    Object findById(String id);

}
