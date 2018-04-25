package com.ecmp.apigateway.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author:wangdayin
 * @date:2018/4/24
 * @remark: 实体类：网关-应用
 */
@Entity
@Table(name = "gateway_application")
public class GatewayApplication extends Domain {
    //应用code
    @Column(name = "application_code", nullable = false, length = 64)
    private String applicationCode;
    //应用名称
    @Column(name = "application_name", nullable = false, length = 50)
    private String applicationName;
    //应用说明
    @Column(name = "application_remark", nullable = false, length = 200)
    private String applicationRemark;

    public GatewayApplication() {
    }

    public GatewayApplication(String applicationCode, String applicationName, String applicationRemark) {
        this.applicationCode = applicationCode;
        this.applicationName = applicationName;
        this.applicationRemark = applicationRemark;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationRemark() {
        return applicationRemark;
    }

    public void setApplicationRemark(String applicationRemark) {
        this.applicationRemark = applicationRemark;
    }
}
