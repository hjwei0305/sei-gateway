package com.ecmp.apigateway.zuul.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 路由刷新-业务类
 */
@Service
public class RefreshService {
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private RouteLocator routeLocator;

    public void refreshRoute() {
        RoutesRefreshedEvent routesRefreshedEvent = new RoutesRefreshedEvent(routeLocator);
        publisher.publishEvent(routesRefreshedEvent);
    }

}
