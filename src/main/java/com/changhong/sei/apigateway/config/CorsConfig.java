package com.changhong.sei.apigateway.config;

import com.changhong.sei.apigateway.globalFilter.CorsResponseHeaderFilter;
import com.changhong.sei.core.constant.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.DefaultCorsProcessor;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

/**
 * usage:
 * User:liusonglin; Date:2018/9/20;ProjectName:api-gateway;
 */
@Configuration
public class CorsConfig {

//    private static final String ALLOWED_HEADERS = "x-requested-with, authorization, x-authorization, x-sid, Content-Type, Authorization, credential, Content-Disposition";
//    private static final String ALLOWED_METHODS = "*";
//    private static final String ALLOWED_ORIGIN = "*";
//    private static final String ALLOWED_EXPOSE = "*";
//    private static final String MAX_AGE = "7200L";
//
//    @Bean
//    public WebFilter corsFilter() {
//        return (ServerWebExchange ctx, WebFilterChain chain) -> {
//            ServerHttpRequest request = ctx.getRequest();
//            if (CorsUtils.isCorsRequest(request)) {
//                ServerHttpResponse response = ctx.getResponse();
//                HttpHeaders headers = response.getHeaders();
//                headers.set("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
//                headers.set("Access-Control-Allow-Methods", ALLOWED_METHODS);
//                headers.set("Access-Control-Max-Age", MAX_AGE);
//                headers.set("Access-Control-Allow-Headers", ALLOWED_HEADERS);
//                headers.set("Access-Control-Expose-Headers", ALLOWED_EXPOSE);
//                headers.set("Access-Control-Allow-Credentials", "true");
//                if (request.getMethod() == HttpMethod.OPTIONS) {
//                    response.setStatusCode(HttpStatus.OK);
//                    return Mono.empty();
//                }
//            }
//            return chain.filter(ctx);
//        };
//    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        // 允许cookies跨域
        config.setAllowCredentials(true);
        // #允许向该服务器提交请求的URI，*表示全部允许，在SpringMVC中，如果设成*，会自动转成当前请求头中的Origin
        config.addAllowedOrigin("*");
        // #允许访问的头信息,*表示全部
        config.addAllowedHeader("*");
        // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.setMaxAge(7200L);
        // 允许提交请求的方法，*表示全部允许
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter((CorsConfigurationSource) source);
    }

    //https://blog.csdn.net/zimou5581/article/details/90043178?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-1
    @Bean
    public CorsResponseHeaderFilter corsResponseHeaderFilter() {
        return new CorsResponseHeaderFilter();
    }

//    @Bean
//    public CorsWebFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
//        source.registerCorsConfiguration("/**", buildCorsConfiguration());
//
//        return new CorsWebFilter(source, new DefaultCorsProcessor() {
//            @Override
//            protected boolean handleInternal(ServerWebExchange exchange, CorsConfiguration config,
//                                             boolean preFlightRequest) {
//                // 预留扩展点
//                // if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
//                return super.handleInternal(exchange, config, preFlightRequest);
//                // }
//
//                // return true;
//            }
//        });
//    }

    private CorsConfiguration buildCorsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");

        corsConfiguration.addAllowedMethod(HttpMethod.OPTIONS);
        corsConfiguration.addAllowedMethod(HttpMethod.POST);
        corsConfiguration.addAllowedMethod(HttpMethod.GET);
        corsConfiguration.addAllowedMethod(HttpMethod.PUT);
        corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
        corsConfiguration.addAllowedMethod(HttpMethod.PATCH);
        // corsConfiguration.addAllowedMethod("*");

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
