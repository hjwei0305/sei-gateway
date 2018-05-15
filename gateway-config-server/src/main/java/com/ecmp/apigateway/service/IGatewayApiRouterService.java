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
     * 获取路由设置
     * @param gatewayApiRouter 路由设置-实体参数
     * @return
     */
    GatewayApiRouter getting(GatewayApiRouter gatewayApiRouter);

    /**
     * 设置路由配置
     * @param gatewayApiRouter 路由配置-实体参数
     */
    void setting(GatewayApiRouter gatewayApiRouter);

    /**
     * 根据应用服务ID删除路由配置
     * @param serviceId 应用服务ID
     */
    void removeByServiceId(String serviceId);

    /**
     * 根据应用服务ID启用|停用路由配置
     * @param serviceId 应用服务ID
     * @param enable 是否启用：true|false
     */
    void enableByServiceId(String serviceId, boolean enable);

    /**
     * 根据应用服务ID查询路由配置
     * @param serviceId 应用服务ID
     * @return
     */
    List<GatewayApiRouter> findByServiceId(String serviceId);

    /**
     * 调用网关路由刷新
     * @return
     */
    Object refresh();
}
