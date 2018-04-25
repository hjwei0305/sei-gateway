package com.ecmp.apigateway.dao;

import com.ecmp.apigateway.model.GatewayApiRouter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 网关-路由配置-持久层接口
 */
public interface GatewayApiRouterDao extends JpaRepository<GatewayApiRouter, String> {

    List<GatewayApiRouter> findAllByDeletedFalse();

    List<GatewayApiRouter> findAllByEnabledTrue();

    @Query(value = "select * from gateway_api_router where path like %?1% or service_id like %?1% or interface_name like %?1% ", nativeQuery = true)
    List<GatewayApiRouter> findAll(String keywords);

    @Query(value = "select * from gateway_api_router where id=?1 ", nativeQuery = true)
    GatewayApiRouter findById(String id);

    @Modifying
    @Query(value = "delete * from gateway_api_router ", nativeQuery = true)
    int removeAll();

    @Modifying
    @Query(value = "delete * from gateway_api_router where id=?1 ", nativeQuery = true)
    int removeById(String id);

}
