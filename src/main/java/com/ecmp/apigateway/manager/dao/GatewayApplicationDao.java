package com.ecmp.apigateway.manager.dao;

import com.ecmp.apigateway.manager.entity.GatewayApplication;
import com.ecmp.core.dao.BaseEntityDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author:wangdayin
 * @date:2018/4/24
 * @remark: 网关应用dao层
 */
@Repository
public interface GatewayApplicationDao extends BaseEntityDao<GatewayApplication> {

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
     * /**
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
    List<GatewayApplication> findByApplicationNameAndDeletedFalse(String applicationName);

    /**
     * 根据id和code查询应用信息
     *
     * @param id              主键ID
     * @param applicationCode 应用code
     * @return
     */
    GatewayApplication findByIdOrApplicationCode(@Param("id") String id, @Param("applicationCode") String applicationCode);


}
