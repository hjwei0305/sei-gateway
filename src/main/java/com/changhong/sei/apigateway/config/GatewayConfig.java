package com.changhong.sei.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.WebFilter;

/**
 * usage:
 * User:liusonglin; Date:2018/9/20;ProjectName:api-gateway;
 */
@Configuration
public class GatewayConfig {

    @Bean
    public WebFilter contextPathWebFilter(Environment env) {
        String contextPath = env.getProperty("server.servlet.context-path", "/api-gateway");
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (request.getURI().getPath().startsWith(contextPath)) {
                return chain.filter(
                        exchange.mutate()
                                .request(request.mutate().contextPath(contextPath).build())
                                .build());
            }
            return chain.filter(exchange);
        };
    }
}
