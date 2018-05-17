package com.ecmp.apigateway.zuul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 路由定位-定位器
 */
@Component
public class RouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {
    private static final  Logger logger = LoggerFactory.getLogger(RouteLocator.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public RouteLocator(ZuulProperties properties) {
        super(null, properties);
    }

    @Override
    public void refresh() {
        doRefresh();
    }

    @Override
    protected Map<String, ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulRoute> routesMap = new LinkedHashMap<>();
        //从application.properties中加载路由信息
        routesMap.putAll(super.locateRoutes());
        //从db中加载路由信息
        routesMap.putAll(locateRoutesFromDB());
        //优化一下配置
        LinkedHashMap<String, ZuulRoute> values = new LinkedHashMap<>();
        for (Map.Entry<String, ZuulRoute> entry : routesMap.entrySet()) {
            String path = entry.getKey();
            //Prepend with slash if not already present.
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            values.put(path, entry.getValue());
        }
        logger.info("values is {}",values);
        return values;
    }

    private Map<String, ZuulRoute> locateRoutesFromDB() {
        Map<String, ZuulRoute> routes = new LinkedHashMap<>();
        final String sql = "select r.id,r.path,r.strip_prefix,r.retryable,r.version,s.service_appurl as url " +
                "from gateway_api_router r left join gateway_api_service s on  r.service_id = s.id\n" +
                " where r.deleted = false and r.enabled = true  ";
        List<ZuulRouteVO> results = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ZuulRouteVO.class));
        for (ZuulRouteVO result : results) {
            if (org.apache.commons.lang3.StringUtils.isBlank(result.getPath()) || org.apache.commons.lang3.StringUtils.isBlank(result.getUrl())) {
                continue;
            }
            ZuulRoute zuulRoute = new ZuulRoute();
            zuulRoute.setId(result.getId());
            zuulRoute.setPath(result.getPath());
            zuulRoute.setRetryable(false);
            zuulRoute.setUrl(result.getUrl());
            zuulRoute.setStripPrefix(true);
            routes.put(zuulRoute.getPath(), zuulRoute);
        }
        return routes;
    }

    public static class ZuulRouteVO {
        /**
         * id
         */
        private String id;
        /**
         * 路由key
         */
        private String routeKey;
        /**
         * 路由路径
         */
        private String path;
        /**
         * 路由应用服务id
         */
        private String serviceId;
        /**
         * 路由指定服务url
         */
        private String url;
        /**
         * 是否重试访问
         */
        private boolean retryAble = false;
        /**
         * 是否启用路由
         */
        private boolean enabled = false;
        /**
         * 是否过滤路由路径前缀
         */
        private boolean stripPrefix = true;
        /**
         * 路由接口名称
         */
        @Column(name = "interface_name")
        private String interfaceName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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
}
