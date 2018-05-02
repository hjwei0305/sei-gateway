package com.ecmp.apigateway.dao;

import com.ecmp.apigateway.model.GatewayApiRouter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 路由配置-持久层接口
 */
public interface GatewayApiRouterDao extends JpaRepository<GatewayApiRouter, String> {

    /**
     * 根据ID查询路由配置
     * @param id 主键ID
     * @return
     */
    GatewayApiRouter findByDeletedFalseAndId(@Param("id") String id);

    /**
     * 根据应用服务ID查询路由配置
     * @param serviceId 应用服务ID
     * @return
     */
    List<GatewayApiRouter> findByDeletedFalseAndServiceId(@Param("serviceId") String serviceId);

    /**
     * 根据应用服务ID查询路由配置
     * @param serviceId 应用服务ID
     * @return
     */
    List<GatewayApiRouter> findByDeletedFalseAndServiceIdIn(@Param("serviceId") String serviceId);

    /**
     * 根据应用服务ID和接口URI查询路由配置
     * @param serviceId 应用服务ID
     * @param url 接口URI
     * @return
     */
    GatewayApiRouter findByServiceIdAndUrl(@Param("serviceId") String serviceId, @Param("url") String url);
}
