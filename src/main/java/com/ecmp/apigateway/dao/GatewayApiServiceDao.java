package com.ecmp.apigateway.dao;

import com.ecmp.apigateway.model.GatewayApiService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 网关-应用服务-持久层接口
 */
public interface GatewayApiServiceDao extends JpaRepository<GatewayApiService, String> {

    List<GatewayApiService> findAllByDeletedFalse();

    @Query(value = "select * from gateway_api_service where service_appname like %?1% or service_appremark like %?1% or service_appversion like %?1% ", nativeQuery = true)
    List<GatewayApiService> findAll(String keywords);

    @Query(value = "select * from gateway_api_service where id=?1 ", nativeQuery = true)
    GatewayApiService findById(String id);

    @Modifying
    @Query(value = "delete * from gateway_api_service ", nativeQuery = true)
    int removeAll();

    @Modifying
    @Query(value = "delete * from gateway_api_service where id=?1 ", nativeQuery = true)
    int removeById(String id);
}
