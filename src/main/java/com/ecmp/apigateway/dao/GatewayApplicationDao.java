package com.ecmp.apigateway.dao;

import com.ecmp.apigateway.model.GatewayApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author:wangdayin
 * @date:2018/4/24
 * @remark:
 */
public interface GatewayApplicationDao extends JpaRepository<GatewayApplication, String> {

    List<GatewayApplication> findAllByDeletedFalse();
}
