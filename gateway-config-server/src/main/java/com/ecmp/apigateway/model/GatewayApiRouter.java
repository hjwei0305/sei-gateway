package com.ecmp.apigateway.model;

import com.ecmp.apigateway.model.common.Domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 路由配置-实体类
 */
@Entity
@Table(name = "gateway_api_router")
public class GatewayApiRouter extends Domain {
    /**
     * 路由key
     */
    @Column(name = "route_key", nullable = false, length = 200)
    private String routeKey;
    /**
     * 路由路径
     */
    @Column(name = "path", nullable = false, length = 200)
    private String path;
    /**
     * 路由应用服务id
     */
    @Column(name = "service_id", nullable = false, length = 64)
    private String serviceId;
    /**
     * 路由指定服务url
     */
    @Column(name = "url")
    private String url;
    /**
     * 是否重试访问
     */
    @Column(name = "retryable")
    private boolean retryAble = false;
    /**
     * 是否启用路由
     */
    @Column(name = "enabled", nullable = false)
    private boolean enabled = false;
    /**
     * 是否过滤路由路径前缀
     */
    @Column(name = "strip_prefix")
    private boolean stripPrefix = true;
    /**
     * 路由接口名称
     */
    @Column(name = "interface_name")
    private String interfaceName;

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isRetryAble() {
        return retryAble;
    }

    public void setRetryAble(boolean retryAble) {
        this.retryAble = retryAble;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isStripPrefix() {
        return stripPrefix;
    }

    public void setStripPrefix(boolean stripPrefix) {
        this.stripPrefix = stripPrefix;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }
}
