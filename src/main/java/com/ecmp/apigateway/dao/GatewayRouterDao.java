package com.ecmp.apigateway.dao;

import com.ecmp.apigateway.model.GatewayRouter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 网关-路由
 */
public interface GatewayRouterDao extends JpaRepository<GatewayRouter, String> {

    List<GatewayRouter> findAllByDeletedFalse();
    List<GatewayRouter> findAllByEnabledTrue();
}
