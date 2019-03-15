package com.ecmp.apigateway.zuul;

import com.ecmp.apigateway.manager.dao.GatewayApiServiceDao;
import com.ecmp.apigateway.manager.entity.GatewayApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 路由定位-定位器
 */
@Component
public class RouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {
    private static final  Logger logger = LoggerFactory.getLogger(RouteLocator.class);

    private static final String PATH_SERPERATE = "/";

    @Autowired
    private GatewayApiServiceDao gatewayApiServiceDao;

    public RouteLocator(ZuulProperties properties) {
        super(null, properties);
    }

    Set<String> sensitiveHeaders = new HashSet<>();

    {
        sensitiveHeaders.add("Access-Control-Allow-Origin");
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
            if (!path.startsWith(PATH_SERPERATE)) {
                path = PATH_SERPERATE + path;
            }
            values.put(path, entry.getValue());
        }
        logger.info("values is {}",values);
        return values;
    }

    private Map<String, ZuulRoute> locateRoutesFromDB() {
        Map<String, ZuulRoute> routes = new LinkedHashMap<>();
        List<GatewayApiService> results = gatewayApiServiceDao.findByDeletedFalseAndServiceAppEnabledTrue();
        for (GatewayApiService result : results) {
            if (org.apache.commons.lang3.StringUtils.isBlank(result.getServicePath()) ||
                    org.apache.commons.lang3.StringUtils.isBlank(result.getServiceAppUrl())) {
                continue;
            }
            ZuulRoute zuulRoute = new ZuulRoute();
            zuulRoute.setId(result.getId());
            zuulRoute.setPath(result.getServicePath());
            zuulRoute.setRetryable(result.isRetryAble());
            zuulRoute.setUrl(result.getServiceAppUrl());
            zuulRoute.setStripPrefix(result.isStripPrefix());
            zuulRoute.setSensitiveHeaders(sensitiveHeaders);
            routes.put(zuulRoute.getPath(), zuulRoute);
        }
        return routes;
    }
}
