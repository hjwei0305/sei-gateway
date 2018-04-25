package com.ecmp.apigateway.zuul.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 业务类：网关-路由刷新
 */
@Service
public class RefreshRouteService {

    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired
    RouteLocator routeLocator;

    public void refreshRoute() {
        RoutesRefreshedEvent routesRefreshedEvent = new RoutesRefreshedEvent(routeLocator);
        publisher.publishEvent(routesRefreshedEvent);
    }

}
