package com.ecmp.apigateway.service;

import com.ecmp.apigateway.model.GatewayApiRouter;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/25
 * @remark: 路由配置-业务层接口
 */
public interface IGatewayApiRouterService {

    /**
     * 新增路由配置
     * @param gatewayApiRouter
     */
    void save(GatewayApiRouter gatewayApiRouter);

    /**
     * 根据应用服务ID删除路由配置
     * @param serviceId
     */
    void removeByServiceId(String serviceId);

    /**
     * 根据应用服务ID启用路由配置
     * @param serviceId
     */
    void enableByServiceId(String serviceId);

    /**
     * 根据应用服务ID查询路由配置
     * @param serviceId
     * @return
     */
    List<GatewayApiRouter> findByServiceId(String serviceId);
}
