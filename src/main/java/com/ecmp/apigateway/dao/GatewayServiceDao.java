package com.ecmp.apigateway.dao;

import com.ecmp.apigateway.model.GatewayService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 网关-服务
 */
public interface GatewayServiceDao extends JpaRepository<GatewayService, String> {

    List<GatewayService> findAllByDeletedFalse();
}
