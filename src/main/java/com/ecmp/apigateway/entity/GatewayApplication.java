package com.ecmp.apigateway.entity;

import com.ecmp.apigateway.entity.common.Domain;
import com.ecmp.core.entity.BaseAuditableEntity;
import com.ecmp.core.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * @author:wangdayin
 * @date:2018/4/24
 * @remark: 实体类：网关-应用
 */
@Entity
@Table(name = "gateway_application")
public class GatewayApplication extends BaseEntity {
    //应用code
    @Column(name = "application_code", nullable = false, length = 64)
    private String applicationCode;
    //应用名称
    @Column(name = "application_name", nullable = false, length = 50)
    private String applicationName;
    //应用说明
    @Column(name = "application_remark", nullable = false, length = 200)
    private String applicationRemark;

    //版本号
    @Column(name = "version", nullable = false)
    @Version
    private int version = 0;
    //是否删除--true：已删除；false：未删除
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
