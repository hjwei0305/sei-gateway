package com.ecmp.apigateway.model;

import javax.persistence.*;

/**
 * @author: hejun
 * @date:2018/4/24
 * @remark: 实体类：网关-服务
 */
@Entity
@Table(name = "gateway_service")
public class GatewayService extends Domain {
    /**
     * 服务应用id
     */
    @Column(name = "service_appid")
    private String serviceAppId;
    /**
     * 服务应用名称
     */
    @Column(name = "service_appname")
    private String serviceAppName;
    /**
     * 服务应用说明
     */
    @Column(name = "service_appremark")
    private String serviceAppRemark;
    /**
     * 服务应用版本
     */
    @Column(name = "service_appversion")
    private String serviceAppVersion;
    /**
     * 应用code
     */
    @JoinTable(name = "gateway_application", joinColumns = { @JoinColumn(name = "application_code") })
    @ManyToOne
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
