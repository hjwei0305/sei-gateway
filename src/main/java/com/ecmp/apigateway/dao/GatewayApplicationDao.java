package com.ecmp.apigateway.dao;

import com.ecmp.apigateway.model.GatewayApplication;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author:wangdayin
 * @date:2018/4/24
 * @remark: 网关应用dao层
 */
public interface GatewayApplicationDao extends JpaRepository<GatewayApplication, String> {

    /**
     * 查询所有网关应用-未分页
     *
     * @return
     */
    List<GatewayApplication> findAllByDeletedFalse();

    List<GatewayApplication> findAllByDeletedFalseAndApplicationNameLike(@Param("applicationName") String keywords, Pageable pageable);

    GatewayApplication findFirstByApplicationNameAndDeletedFalse(String applicationName);



}
