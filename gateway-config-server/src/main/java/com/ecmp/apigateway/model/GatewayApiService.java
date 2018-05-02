package com.ecmp.apigateway.model;

import com.ecmp.apigateway.model.common.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 应用服务-实体类
 */
@Entity
@Table(name = "gateway_api_service")
public class GatewayApiService extends Domain {
    /**
     * 应用服务id
     */
    @Column(name = "service_appid", nullable = false, length = 64)
    private String serviceAppId;
    /**
     * 应用服务名称
     */
    @Column(name = "service_appname", nullable = false, length = 50)
    private String serviceAppName;
    /**
     * 应用服务说明
     */
    @Column(name = "service_appremark")
    private String serviceAppRemark;
    /**
     * 应用服务版本
     */
    @Column(name = "service_appversion", nullable = false, length = 50)
    private String serviceAppVersion;
    /**
     * 应用服务是否启用路由
     */
    @Column(name = "service_appenabled", nullable = false)
    private Boolean serviceAppEnabled = false;
    /**
     * 接口应用code-关联字段
     */
    @Column(name = "application_code", nullable = false, length = 64)
    private String applicationCode;

    public String getServiceAppId() {
        return serviceAppId;
    }

    public void setServiceAppId(String serviceAppId) {
        this.serviceAppId = serviceAppId;
    }

    public String getServiceAppName() {
        return serviceAppName;
    }

    public void setServiceAppName(String serviceAppName) {
        this.serviceAppName = serviceAppName;
    }

    public String getServiceAppRemark() {
        return serviceAppRemark;
    }

    public void setServiceAppRemark(String serviceAppRemark) {
        this.serviceAppRemark = serviceAppRemark;
    }

    public String getServiceAppVersion() {
        return serviceAppVersion;
    }

    public void setServiceAppVersion(String serviceAppVersion) {
        this.serviceAppVersion = serviceAppVersion;
    }

    public Boolean getServiceAppEnabled() {
        return serviceAppEnabled;
    }

    public void setServiceAppEnabled(Boolean serviceAppEnabled) {
        this.serviceAppEnabled = serviceAppEnabled;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }
}
