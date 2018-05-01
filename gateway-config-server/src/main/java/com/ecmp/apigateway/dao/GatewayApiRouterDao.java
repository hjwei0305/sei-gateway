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
     * 根据应用服务ID查询
     * @param serviceId
     * @return
     */
    List<GatewayApiRouter> findByDeletedFalseAndServiceId(@Param("serviceId") String serviceId);
}
