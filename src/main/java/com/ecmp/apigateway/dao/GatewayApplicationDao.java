package com.ecmp.apigateway.dao;

import com.ecmp.apigateway.model.GatewayApplication;
import org.springframework.data.domain.Page;
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
    List<GatewayApplication> findByDeletedFalse();

    /**
     * 分页查询网关应用-分页
     *
     * @param keywords 关键字
     * @param pageable 分页
     * @return
     */
    Page<GatewayApplication> findByDeletedFalseAndApplicationNameLike(@Param("applicationName") String keywords, Pageable pageable);

    /**
     * 分页查询网关应用-分页 无关键字
     *
     * @param pageable
     * @return
     */
    Page<GatewayApplication> findByDeletedFalse(Pageable pageable);

    /**
     * 根据应用名称查询
     *
     * @param applicationName 应用名
     * @return
     */
    GatewayApplication findFirstByApplicationNameAndDeletedFalse(String applicationName);

    /**
     * 根据id和code查询应用信息
     *
     * @param id              主键ID
     * @param applicationCode 应用code
     * @return
     */
    GatewayApplication findByIdOrAndApplicationCode(@Param("id") String id, @Param("applicationCode") String applicationCode);



}
