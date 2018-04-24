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
    @Column(name = "application_code")
    private String applicationCode;
    //应用名称
    @Column(name = "application_name")
    private String applicationName;
    //应用说明
    @Column(name = "application_remark")
    private String applicationRemark;

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
