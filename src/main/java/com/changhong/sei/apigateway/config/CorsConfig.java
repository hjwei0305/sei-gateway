package com.changhong.sei.apigateway.config;

import com.changhong.sei.apigateway.globalFilter.CorsResponseHeaderFilter;
import com.changhong.sei.core.constant.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.WebFilter;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * usage:
 * User:liusonglin; Date:2018/9/20;ProjectName:api-gateway;
 */
@Configuration
public class CorsConfig {

    //https://blog.csdn.net/zimou5581/article/details/90043178?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1
    @Bean
    public CorsResponseHeaderFilter corsResponseHeaderFilter() {
        return new CorsResponseHeaderFilter();
    }

    @Bean
    public CorsWebFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", buildCorsConfiguration());
        return new CorsWebFilter(source);
    }

    private CorsConfiguration buildCorsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");

//        corsConfiguration.addAllowedMethod(HttpMethod.OPTIONS);
//        corsConfiguration.addAllowedMethod(HttpMethod.POST);
//        corsConfiguration.addAllowedMethod(HttpMethod.GET);
//        corsConfiguration.addAllowedMethod(HttpMethod.PUT);
//        corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
//        corsConfiguration.addAllowedMethod(HttpMethod.PATCH);
         corsConfiguration.addAllowedMethod("*");

        corsConfiguration.addAllowedHeader("origin");
        corsConfiguration.addAllowedHeader("content-type");
        corsConfiguration.addAllowedHeader("accept");
        corsConfiguration.addAllowedHeader("x-requested-with");
        corsConfiguration.addAllowedHeader("Referer");
        corsConfiguration.addAllowedHeader("x-sid");
        corsConfiguration.addAllowedHeader("x-authorization");
        corsConfiguration.addAllowedHeader("authorization");
        corsConfiguration.addAllowedHeader("content-disposition");
        corsConfiguration.addAllowedHeader(Constants.HEADER_TOKEN_KEY);
        corsConfiguration.addAllowedHeader(Constants.TRACE_ID);
        corsConfiguration.addAllowedHeader(Constants.TRACE_FROM_SERVER);
        corsConfiguration.addAllowedHeader(Constants.TRACE_CURRENT_SERVER);
        // corsConfiguration.addAllowedHeader("*");

        corsConfiguration.setMaxAge(7200L);
        corsConfiguration.setAllowCredentials(true);
        return corsConfiguration;
    }

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
