package com.ecmp.apigateway.dao;

import com.ecmp.apigateway.model.GatewayApiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 应用服务-持久层接口
 */
public interface GatewayApiServiceDao extends JpaRepository<GatewayApiService, String> {

    /**
     * 查询所有应用服务(不分页)
     * @return
     */
    List<GatewayApiService> findByDeletedFalse();

    /**
     * 查询所有应用服务(分页)
     * @param pageable 分页
     * @return
     */
    Page<GatewayApiService> findByDeletedFalse(Pageable pageable);

    /**
     * 根据关键字查询应用服务(分页)
     * @param kwd0 关键字
     * @param kwd1 关键字
     * @param kwd2 关键字
     * @param pageable 分页
     * @return
     */
    Page<GatewayApiService> findByDeletedFalseAndServiceAppNameLikeOrServiceAppRemarkLikeOrServiceAppVersionLike
    (@Param("serviceAppName") String kwd0, @Param("serviceAppRemark") String kwd1, @Param("serviceAppVersion") String kwd2, Pageable pageable);

    /**
     * 根据ID查询应用服务
     * @param id 主键ID
     * @return
     */
    GatewayApiService findByDeletedFalseAndId(@Param("id") String id);

    /**
     * 根据ID查询应用服务
     * @param id 主键ID
     * @return
     */
    List<GatewayApiService> findByDeletedFalseAndIdIn(@Param("id") List<String> id);

    /**
     * 未删除可用的服务信息
     * @return
     */
    List<GatewayApiService> findByDeletedFalseAndServiceAppEnabledTrue();
}
