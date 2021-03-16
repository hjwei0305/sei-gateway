package com.changhong.sei.apigateway.filter;

import com.changhong.sei.apigateway.commons.Constants;
import com.changhong.sei.apigateway.service.accesslog.AccessLogVo;
import com.changhong.sei.apigateway.service.accesslog.AccessLogProducer;
import com.changhong.sei.core.context.ContextUtil;
import com.changhong.sei.util.IdGenerator;
import com.changhong.sei.util.thread.ThreadLocalUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Objects;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-01-12 13:33
 */
@Component
public class AccessLogGlobalFilter implements GlobalFilter, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(AccessLogGlobalFilter.class);

    @Autowired(required = false)
    private AccessLogProducer producer;

    @Override
    public int getOrder() {
        // start the timer as soon as possible and report the metric event before we write
        // response to client Access
        return 1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 记录开始时间
        exchange.getAttributes().put(Constants.REQUEST_ATTRIBUTE_START_TIME, System.currentTimeMillis());
        // 跟踪埋点
        String traceId = IdGenerator.uuid();
        //链路信息处理
        ServerHttpRequest request = exchange.getRequest().mutate().header(ThreadLocalUtil.TRAN_PREFIX.concat(ContextUtil.TRACE_ID), traceId).build();
        ServerWebExchange webExchange = exchange.mutate().request(request).build();

        return chain.filter(webExchange).then(Mono.fromRunnable(() -> {

            if (Objects.isNull(producer)) {
                return;
            }

            // 获取token. 只对有token的请求做记录
            String token = exchange.getAttribute(Constants.REQUEST_ATTRIBUTE_TOKEN);
            if (StringUtils.isNotBlank(token)) {
                try {
                    long currentTime = System.currentTimeMillis();
                    String path = exchange.getRequest().getURI().getRawPath();

                    AccessLogVo accessLogVo = new AccessLogVo(traceId, token, path, request.getMethodValue());
                    accessLogVo.setAccessTime(currentTime);
                    // 获取开始时间
                    Long startTime = exchange.getAttribute(Constants.REQUEST_ATTRIBUTE_START_TIME);
                    if (Objects.nonNull(startTime)) {
                        // 耗时
                        accessLogVo.setDuration(currentTime - startTime);
                    }
                    // 取路由名作为模块名
                    Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
                    if (Objects.nonNull(route)) {
                        accessLogVo.setAppModule(route.getUri().getHost());
                    }
                    // 实际访问地址
                    URI uri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
                    if (Objects.nonNull(uri)) {
                        accessLogVo.setUrl(uri.toString()).setPath(uri.getPath());
                    }
                    HttpHeaders headers = request.getHeaders();
                    accessLogVo.setIp(getIp(request)).setUserAgent(headers.getFirst("user-agent"));

                    producer.send(accessLogVo);
                } catch (Exception e) {
                    LOG.error("访问日志记录异常", e);
                }
            }
        }));
    }

    private String getIp(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }

        return ip.replaceAll(":", ".");
    }
}
