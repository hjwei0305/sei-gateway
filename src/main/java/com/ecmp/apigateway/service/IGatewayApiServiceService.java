package com.ecmp.apigateway.service;

import com.ecmp.apigateway.model.GatewayApiService;
import com.ecmp.apigateway.model.SearchParam;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/25
 * @remark: 网关-应用服务-业务层接口
 */
public interface IGatewayApiServiceService {

    /**
     * 新增
     * @param gatewayApiService
     */
    void save(GatewayApiService gatewayApiService);

    /**
     * 编辑
     * @param gatewayApiService
     */
    void edit(GatewayApiService gatewayApiService);

    /**
     * 删除所有
     */
    void removeAll();

    /**
     * 根据ID、应用ID删除
     * @param id
     * @param serviceAppId
     */
    void removeById(String id, String serviceAppId);

    /**
     * 查询所有
     * @return
     */
    List<GatewayApiService> findAll();

    /**
     * 分页查询
     * @param searchParam
     * @return
     */
    Page<GatewayApiService> findAllByPage(SearchParam searchParam);

    /**
     * 根据ID、应用ID查询
     * @param id
     * @param serviceAppid
     * @return
     */
    GatewayApiService findById(String id, String serviceAppid);

}
