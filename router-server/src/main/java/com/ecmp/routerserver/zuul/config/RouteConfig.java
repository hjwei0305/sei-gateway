package com.ecmp.routerserver.zuul.config;

import com.ecmp.routerserver.zuul.RouteLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 路由配置-配置类
 */
@Configuration
public class RouteConfig {

    @Autowired
    ZuulProperties zuulProperties;
    @Autowired
    ServerProperties server;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Bean
    public RouteLocator routeLocator() {
        RouteLocator routeLocator = new RouteLocator(this.server.getServletPrefix(), this.zuulProperties);
        routeLocator.setJdbcTemplate(jdbcTemplate);
        return routeLocator;
    }

}
