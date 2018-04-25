package com.ecmp.apigateway.service;

import com.ecmp.apigateway.model.GatewayApiRouter;
import com.ecmp.apigateway.model.SearchParam;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/25
 * @remark: 网关-路由配置-业务层接口
 */
public interface IGatewayApiRouterService {

    /**
     * 新增
     * @param gatewayApiRouter
     */
    void save(GatewayApiRouter gatewayApiRouter);

    /**
     * 编辑
     * @param gatewayApiRouter
     */
    void edit(GatewayApiRouter gatewayApiRouter);

    /**
     * 删除所有
     */
    void removeAll();

    /**
     * 根据ID删除
     * @param id
     */
    void removeById(String id);

    /**
     * 查询所有
     * @return
     */
    List<GatewayApiRouter> findAll();

    /**
     * 分页查询
     * @param searchParam
     * @return
     */
    Page<GatewayApiRouter> findAllByPage(SearchParam searchParam);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    GatewayApiRouter findById(String id);

}
