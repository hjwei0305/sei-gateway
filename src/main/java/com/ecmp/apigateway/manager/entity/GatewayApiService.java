package com.ecmp.apigateway.manager.entity;

import com.ecmp.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 应用服务-实体类
 * @update:liusonglin 新增路由配置字段
 */
@Entity
@Table(name = "gateway_api_service")
public class GatewayApiService extends BaseEntity {
    /**
     * 应用服务id
     */
    @Column(name = "service_appid", nullable = false, length = 64)
    private String serviceAppId;
    /**
     * 应用服务代码
     */
    @Column(name = "service_appcode", nullable = false, length = 50)
    private String serviceAppCode;
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
     * 应用服务url
     */
    @Column(name = "service_appurl")
    private String serviceAppUrl;

    /**
     * 路由匹配路径
     */
    @Column(name = "service_path")
    private String servicePath;

    /**
     * 是否重试访问
     */
    @Column(name = "retry_able")
    private boolean retryAble;

    /**
     * 是否过滤路由路径前缀
     */
    @Column(name = "strip_prefix")
    private boolean stripPrefix;

    /**
     * 应用服务是否启用路由
     */
    @Column(name = "service_appenabled", nullable = false)
    private boolean serviceAppEnabled = false;
    /**
     * 接口应用code-关联字段
     */
    @Column(name = "application_code", nullable = false, length = 64)
    private String applicationCode;

    @OneToMany(mappedBy = "applicationCode",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<GatewayInterface> gatewayInterfaceList;
    //版本号
    @Column(name = "version", nullable = false)
    @Version
    private int version = 0;
    //是否删除--true：已删除；false：未删除
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public String getServiceAppId() {
        return serviceAppId;
    }

    public void setServiceAppId(String serviceAppId) {
        this.serviceAppId = serviceAppId;
    }

    public String getServiceAppCode() {
        return serviceAppCode;
    }

    public void setServiceAppCode(String serviceAppCode) {
        this.serviceAppCode = serviceAppCode;
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

    public String getServiceAppUrl() {
        return serviceAppUrl;
    }

    public void setServiceAppUrl(String serviceAppUrl) {
        this.serviceAppUrl = serviceAppUrl;
    }

    public boolean isServiceAppEnabled() {
        return serviceAppEnabled;
    }

    public void setServiceAppEnabled(boolean serviceAppEnabled) {
        this.serviceAppEnabled = serviceAppEnabled;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public String getServicePath() {
        return servicePath;
    }

    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }

    public boolean isRetryAble() {
        return retryAble;
    }

    public void setRetryAble(boolean retryAble) {
        this.retryAble = retryAble;
    }

    public boolean isStripPrefix() {
        return stripPrefix;
    }

    public void setStripPrefix(boolean stripPrefix) {
        this.stripPrefix = stripPrefix;
    }

    public List<GatewayInterface> getGatewayInterfaceList() {
        return gatewayInterfaceList;
    }

    public void setGatewayInterfaceList(List<GatewayInterface> gatewayInterfaceList) {
        this.gatewayInterfaceList = gatewayInterfaceList;
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
