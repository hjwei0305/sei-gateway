package com.ecmp.apigateway.model;

import javax.persistence.*;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 网关-应用服务-实体类
 */
@Entity
@Table(name = "gateway_api_service")
public class GatewayApiService extends Domain {
    /**
     * 应用服务id
     */
    @Column(name = "service_appid")
    private String serviceAppId;
    /**
     * 应用服务名称
     */
    @Column(name = "service_appname")
    private String serviceAppName;
    /**
     * 应用服务说明
     */
    @Column(name = "service_appremark")
    private String serviceAppRemark;
    /**
     * 应用服务版本
     */
    @Column(name = "service_appversion")
    private String serviceAppVersion;
    /**
     * 应用code-关联字段
     */
    @Column(name = "application_code")
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

    public String getApplicationCode() {
        return applicationCode;
    }
    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }
}
