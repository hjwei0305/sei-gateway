package com.ecmp.apigateway.dao;

import com.ecmp.apigateway.model.GatewayApiRouter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 路由配置-持久层接口
 */
public interface GatewayApiRouterDao extends JpaRepository<GatewayApiRouter, String> {

    /**
     * 查询所有数据(不分页)
     * @return
     */
    List<GatewayApiRouter> findByDeletedFalse();

    /**
     * 查询所有数据(分页)
     * @param pageable
     * @return
     */
    Page<GatewayApiRouter> findByDeletedFalse(Pageable pageable);

    /**
     * 根据关键字查询数据(分页)
     * @param path
     * @param serviceId
     * @param interfaceName
     * @param pageable
     * @return
     */
    Page<GatewayApiRouter> findByDeletedFalseAndPathLikeOrServiceIdLikeOrInterfaceNameLike(@Param("path")String path, @Param("serviceId")String serviceId, @Param("interfaceName")String interfaceName, Pageable pageable);

    /**
     * 根据ID查询数据
     * @param id
     * @return
     */
    GatewayApiRouter findById(@Param("id")String id);

}
