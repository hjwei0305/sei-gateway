package com.ecmp.apigateway.zuul;

import com.ecmp.apigateway.manager.dao.GatewayApiServiceDao;
import com.ecmp.apigateway.manager.entity.GatewayApiService;
import com.ecmp.apigateway.zuul.service.InterfaceService;
import org.apache.commons.lang3.StringUtils;
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
    private static final Logger logger = LoggerFactory.getLogger(RouteLocator.class);

    private static final String PATH_SERPERATE = "/";

    @Autowired
    private GatewayApiServiceDao gatewayApiServiceDao;
    @Autowired
    private InterfaceService interfaceService;

    public RouteLocator(ZuulProperties properties) {
        super(null, properties);
    }

    @Override
    public void refresh() {
        interfaceService.loadRuntimeData(null);

        doRefresh();
    }

    @Override
    protected Map<String, ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulRoute> routesMap = new LinkedHashMap<>();
        //从application.properties中加载路由信息
        routesMap.putAll(super.locateRoutes());
        //从db中加载路由信息
        routesMap.putAll(locateRoutesFromDB());
        logger.info("values is {}", routesMap);
        return routesMap;
    }

    private Map<String, ZuulRoute> locateRoutesFromDB() {
        Set<String> sensitiveHeaders = new HashSet<>();
        sensitiveHeaders.add("Access-Control-Allow-Origin");

        Map<String, ZuulRoute> routes = new LinkedHashMap<>();
        List<GatewayApiService> results = gatewayApiServiceDao.findByDeletedFalseAndServiceAppEnabledTrue();
        if (results != null && results.size() > 0) {
            results.forEach(result -> {
                if (StringUtils.isNotBlank(result.getServicePath())
                        && StringUtils.isNotBlank(result.getServiceAppUrl())) {
                    String path = result.getServicePath();
                    if (!path.startsWith(PATH_SERPERATE)) {
                        path = PATH_SERPERATE + path;
                    }
                    ZuulRoute zuulRoute = new ZuulRoute();
                    zuulRoute.setId(result.getId());
                    zuulRoute.setPath(path);
                    zuulRoute.setRetryable(result.isRetryAble());
                    zuulRoute.setUrl(result.getServiceAppUrl());
                    zuulRoute.setStripPrefix(result.isStripPrefix());
                    zuulRoute.setSensitiveHeaders(sensitiveHeaders);
                    routes.put(path, zuulRoute);
                }
            });
        }
        return routes;
    }
}