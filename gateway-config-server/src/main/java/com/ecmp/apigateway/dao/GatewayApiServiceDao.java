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
     * 查询所有数据(不分页)
     *
     * @return
     */
    List<GatewayApiService> findByDeletedFalse();

    /**
     * 查询所有数据(分页)
     *
     * @param pageable
     * @return
     */
    Page<GatewayApiService> findByDeletedFalse(Pageable pageable);

    /**
     * 根据关键字查询数据(分页)
     *
     * @param kwd0
     * @param kwd1
     * @param kwd2
     * @param pageable
     * @return
     */
    Page<GatewayApiService> findByDeletedFalseAndServiceAppNameLikeOrServiceAppRemarkLikeOrServiceAppVersionLike(@Param("serviceAppName") String kwd0, @Param("serviceAppRemark") String kwd1, @Param("serviceAppVersion") String kwd2, Pageable pageable);

    /**
     * 根据ID、应用ID查询数据
     *
     * @param id
     * @param serviceAppId
     * @return
     */
    GatewayApiService findByIdOrServiceAppId(@Param("id") String id, @Param("serviceAppId") String serviceAppId);

}
