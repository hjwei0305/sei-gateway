package com.changhong.sei.core.config;

import com.changhong.sei.apigateway.commons.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-01-19 00:55
 */
public class SwaggerConfiguration {
    private final Logger log = LoggerFactory.getLogger(SwaggerConfiguration.class);

    /**
     * 服务发现的路由处理器
     */
    @Autowired
    private RouteLocator routeLocator;

    /**
     * 网关应用名称
     */
    @Value("${spring.application.name}")
    private String self;
    /**
     * 网关应用上下文
     */
    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Bean
    public SwaggerResourcesProvider swaggerResourcesProvider() {
        return () -> {
            List<SwaggerResource> resources = new ArrayList<>();
            // 记录已经添加过的server，存在同一个应用注册了多个服务
            Set<String> dealed = new HashSet<>();

            routeLocator.getRoutes()
                    // 排除host为空的
                    .filter(route -> route.getUri().getHost() != null)
                    // 排除自己
                    .filter(route -> !self.equals(route.getUri().getHost()) && !dealed.contains(route.getUri().getHost()))
                    .subscribe(route -> {
                        dealed.add(route.getUri().getHost());
                        resources.add(swaggerResource(route.getUri().getHost(), route.getUri()));
                    });

            return resources;
        };
    }

    private SwaggerResource swaggerResource(String name, URI uri) {
        String location =  uri.getHost().concat(Constants.SWAGGER2URL);

//        log.info("name:{},location:{}", name, location);
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}
