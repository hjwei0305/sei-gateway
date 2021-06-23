package com.changhong.sei.apigateway.filter;

import com.changhong.sei.apigateway.commons.Constants;
import com.changhong.sei.apigateway.service.AuthWhitelistService;
import com.changhong.sei.apigateway.service.client.AuthServiceClient;
import com.changhong.sei.core.dto.ResultData;
import com.changhong.sei.core.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private AuthWhitelistService interfaceService;
    @Autowired
    private Environment env;
    @Resource
    private AuthServiceClient authServiceClient;

    @Value("${sei.header.token:x-authorization}")
    public String internalTokenKey;
    @Value("${sei.header.sid:x-sid}")
    public String sessionIdKey;
    @Value("${sei.cookie.domain:none}")
    public String cookieDomain;
    public static final String sei3SID = "_s";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String uri = request.getPath().toString();
//        System.out.println("当前访问:" + uri);
        // 平台全局忽略会话检查的地址.后期可以改为网关的配置读取
        if (StringUtils.containsAny(uri, Constants.SWAGGER2URL, "/sso/", "/version/", "/monitor/health", "/edm-service/pdfjs/", "/websocket/", "doc.html", "/swagger-resources", "/v2/api-docs")
                || StringUtils.endsWithAny(uri, ".js", ".css", ".ico")) {
            //return chain.filter(exchange);
            ServerHttpRequest internalRequest = request.mutate().contextPath("/").build();
            ServerWebExchange internalExchange = exchange.mutate().request(internalRequest).response(response).build();
            return chain.filter(internalExchange);
        }
        // 获取sid
        String sid = getSid(request);
        ResultData<String> result;
        String internalToken;
        if (StringUtils.isBlank(sid)) {
            HttpHeaders headers = request.getHeaders();
            // token认证
            String token = headers.getFirst(internalTokenKey);
            if (StringUtils.isBlank(token)) {
                token = headers.getFirst(HttpHeaders.AUTHORIZATION);
            }
            if (StringUtils.isNotBlank(token)) {
                internalToken = token;
            } else {
                // 没有会话id,先判断接口是否需要认证，不需要认证接口直接请求内部token
                if (shouldFilter(uri)) {
                    return buildResultHeader(response, HttpStatus.UNAUTHORIZED, "未在请求中找到有效token");
                } else {
                    result = authServiceClient.getAnonymousToken();
                    if (result.successful()) {
                        internalToken = result.getData();
                    } else {
                        return buildResultHeader(response, HttpStatus.UNAUTHORIZED, result.getMessage());
                    }
                }
            }
        } else {
            // 有会话id,直接校验会话，如果校验成功,带上会话信息请求，
            // 如果校验失败,先判断接口是否需要认证，不需要认证接口直接请求内部token
            result = authServiceClient.check(sid);
            if (result.successful()) {
                internalToken = result.getData();

                /*
                 由于运行环境的稳定性问题(网络\内存等)导致内部调用失败,而这部分问题是瞬时的一般能快速恢复,如果直接返回401给用户造成频繁的登录.
                 故,在此按超时容错降级处理返回http status 408
                 */
                if (AuthServiceClient.INTERNAL_ERROR.equals(internalToken)) {
                    return buildResultHeader(response, HttpStatus.REQUEST_TIMEOUT, result.getMessage());
                }
                // 设置cookie
                cookieWrite(request, response, sid);
            } else {
                if (shouldFilter(uri)) {
                    return buildResultHeader(response, HttpStatus.UNAUTHORIZED, result.getMessage());
                }

                result = authServiceClient.getAnonymousToken();
                if (result.successful()) {
                    internalToken = result.getData();
                } else {
                    return buildResultHeader(response, HttpStatus.UNAUTHORIZED, result.getMessage());
                }
            }
        }
        // 如果没有内部token生成,账号服务可能出错
        if (StringUtils.isBlank(internalToken)) {
            return buildResultHeader(response, HttpStatus.UNAUTHORIZED, "获取认证信息出错，请联系管理员");
        }

        // 把内部token放入header
        ServerHttpRequest internalRequest = request.mutate().header(this.internalTokenKey, internalToken).contextPath("/").build();
        ServerWebExchange internalExchange = exchange.mutate().request(internalRequest).response(response).build();
        return chain.filter(internalExchange);
    }

    private boolean shouldFilter(String uri) {
        String contextPath = env.getProperty("server.servlet.context-path", "/api-gateway");
        Boolean checkToken = interfaceService.checkToken(uri.replaceAll(contextPath, ""));
        log.info("uri: {}, 是否需要Token检查: {}", uri, checkToken);
        return checkToken;
    }

    public Mono<Void> buildResultHeader(ServerHttpResponse response, HttpStatus status, String msg) {
        ResultData<String> resultData = ResultData.fail(msg);
        byte[] bits = JsonUtils.toJson(resultData).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(status);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().set("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -100;
    }

    private String getSid(ServerHttpRequest request) {
        String sid = request.getHeaders().getFirst(sessionIdKey);
        if (StringUtils.isBlank(sid)) {
            sid = request.getHeaders().getFirst(sei3SID);
            if (StringUtils.isBlank(sid)) {
                sid = request.getQueryParams().getFirst(sei3SID);
//                if (StringUtils.isBlank(sid)) {
//                    MultiValueMap<String, HttpCookie> cookieMap = request.getCookies();
//                    HttpCookie httpCookie = cookieMap.getFirst(sessionIdKey);
//                    if (Objects.nonNull(httpCookie)) {
//                        byte[] encodedCookieBytes = Base64.getDecoder().decode(httpCookie.getValue());
//                        sid = new String(encodedCookieBytes);
//                    } else {
//                        httpCookie = cookieMap.getFirst(sei3SID);
//                        if (Objects.nonNull(httpCookie)) {
//                            byte[] encodedCookieBytes = Base64.getDecoder().decode(httpCookie.getValue());
//                            sid = new String(encodedCookieBytes);
//                        }
//                    }
//                }
            }
        }
        return sid;
    }

    private void cookieWrite(ServerHttpRequest request, ServerHttpResponse response, String value) {
        byte[] encodedCookieBytes = Base64.getEncoder().encode(value.getBytes());
        value = new String(encodedCookieBytes);

        String domain = "none".equalsIgnoreCase(cookieDomain) ? "" : cookieDomain;
        boolean secure = "https".equalsIgnoreCase(request.getURI().getScheme());

        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(sessionIdKey, value)
                .path("/")
                .maxAge(-1)
                .httpOnly(true);
        if (StringUtils.isNotBlank(domain)) {
            cookieBuilder.domain(domain);
        }
        if (secure) {
            //https://blog.csdn.net/weixin_44269886/article/details/102459425?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-2&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-2
            cookieBuilder.sameSite("None").secure(secure);
        }
        response.addCookie(cookieBuilder.build());

        cookieBuilder = ResponseCookie.from(sei3SID, value)
                .path("/")
                .maxAge(-1)
                .httpOnly(true);
        if (StringUtils.isNotBlank(domain)) {
            cookieBuilder.domain(domain);
        }
        if (secure) {
            cookieBuilder.sameSite("None").secure(secure);
        }
        response.addCookie(cookieBuilder.build());
    }
}
