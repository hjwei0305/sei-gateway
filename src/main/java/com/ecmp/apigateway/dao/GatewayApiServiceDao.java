package com.ecmp.apigateway.dao;

import com.ecmp.apigateway.model.GatewayApiService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 网关-应用服务-持久层接口
 */
public interface GatewayApiServiceDao extends JpaRepository<GatewayApiService, String> {

    List<GatewayApiService> findAllByDeletedFalse();
}
