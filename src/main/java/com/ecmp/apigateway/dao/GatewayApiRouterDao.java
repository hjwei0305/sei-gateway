package com.ecmp.apigateway.dao;

import com.ecmp.apigateway.model.GatewayApiRouter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 网关-路由配置
 */
public interface GatewayApiRouterDao extends JpaRepository<GatewayApiRouter, String> {

    List<GatewayApiRouter> findAllByDeletedFalse();

    List<GatewayApiRouter> findAllByEnabledTrue();
}
