package com.ecmp.apigateway.service;

import com.ecmp.apigateway.model.GatewayApiService;
import com.ecmp.apigateway.model.common.SearchParam;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/25
 * @remark: 应用服务-业务层接口
 */
public interface IGatewayApiServiceService {

    /**
     * 新增应用服务
     * @param gatewayApiService
     */
    void save(GatewayApiService gatewayApiService);

    /**
     * 编辑应用服务
     * @param gatewayApiService
     */
    void edit(GatewayApiService gatewayApiService);

    /**
     * 根据ID删除应用服务
     * @param id 主键ID
     */
    void removeById(String id);

    /**
     * 根据ID修改应用服务启用状态
     * @param id 主键ID
     */
    void startById(String id);

    /**
     * 根据ID修改应用服务停用状态
     * @param id 主键ID
     */
    void stopById(String id);

    /**
     * 查询所有应用服务
     * @return
     */
    List<GatewayApiService> findAll();

    /**
     * 分页查询应用服务
     * @param searchParam
     * @return
     */
    Page<GatewayApiService> findAllByPage(SearchParam searchParam);

    /**
     * 根据ID查询应用服务
     * @param id 主键ID
     * @return
     */
    GatewayApiService findById(String id);
}
