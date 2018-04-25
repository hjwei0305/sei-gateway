package com.ecmp.apigateway.dao;

import com.ecmp.apigateway.model.GatewayInterface;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author:wangdayin
 * @date:2018/4/24
 * @remark:网关-接口dao层
 */
public interface GatewayInterfaceDao extends JpaRepository<GatewayInterface, String> {
}
