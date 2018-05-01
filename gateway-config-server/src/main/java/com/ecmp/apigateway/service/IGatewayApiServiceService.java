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
     * 根据ID删除
     * @param id
     */
    void removeById(String id);

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
     * 根据ID查询
     * @param id
     * @return
     */
    GatewayApiService findById(String id);
}
