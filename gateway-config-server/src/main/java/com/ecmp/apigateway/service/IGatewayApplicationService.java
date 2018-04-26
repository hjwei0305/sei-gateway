package com.ecmp.apigateway.service;


import com.ecmp.apigateway.enums.OperationTypeEnum;
import com.ecmp.apigateway.model.GatewayApplication;
import com.ecmp.apigateway.model.SearchParam;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author:wangdayin
 * @date:2018/4/24
 * @remark:网关应用业务接口
 */
public interface IGatewayApplicationService {

    /**
     * 新增网关-应用
     *
     * @param gatewayApplication 参数：应用实体
     */
    void addGatewayApplication(GatewayApplication gatewayApplication);

    /**
     * 修改网关-应用
     *
     * @param gatewayApplication 参数：应用实体
     */
    void modifyGatewayApplication(GatewayApplication gatewayApplication);

    /**
     * 移除网关-应用
     *
     * @param id              应用ID
     * @param applicationCode 应用code
     */
    void removeGatewayApplication(String id, String applicationCode);

    /**
     * 查询所有网关应用-未分页
     *
     * @return
     */
    List<GatewayApplication> findAll();

    /**
     * 分页查询网关应用-分页
     *
     * @param searchParam 查询参数
     * @return
     */
    Page<GatewayApplication> findAllByKeywordAndPage(SearchParam searchParam);

    /**
     * 根据应用名称查询
     *
     * @param applicationName 应用名
     * @return
     */
    List<GatewayApplication> findGatewayApplicationByName(String applicationName);

    /**
     * 根据id或者code查询网关应用
     *
     * @param id              ID
     * @param applicationCode 应用code
     * @return
     */
    GatewayApplication findGatewayApplicationByIdOrCode(String id, String applicationCode);

    /**
     * 根据操作类型检查应用名称
     *
     * @param applicationName
     * @param operationType
     * @return
     */
    boolean checkApplicationName(String applicationName, OperationTypeEnum operationType);
}
