package com.changhong.sei.apigateway.filter;

import com.changhong.sei.apigateway.service.InterfaceService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private InterfaceService interfaceService;
    @Autowired
    private Environment env;
    @Resource
    private AuthServiceClient authServiceClient;

    @Value("${sei.header.token:x-authorization}")
    public String internalTokenKey;
    @Value("${sei.header.sid:x-sid}")
    public String sessionIdKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String uri = request.getPath().toString();
        // 平台全局忽略会话检查的地址.后期可以改为网关的配置读取
        if (StringUtils.containsAny(uri, "/sso/", "/monitor/health", "/edm-service/pdfjs/", "/websocket/log")) {
            return chain.filter(exchange);
        }
        // 获取sid
        String sid = getSid(request);
        ResultData<String> result;
        String internalToken;
        if (StringUtils.isBlank(sid)) {
            // TODO 兼容SEI3.0认证token
            String token3_0 = request.getHeaders().getFirst("Authorization");
            if (StringUtils.isNotBlank(token3_0)) {
                internalToken = token3_0;
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
//        System.out.println("内部token: "+internalHeader+" = " + internalToken);
        ServerHttpRequest internalRequest = request.mutate().header(this.internalTokenKey, internalToken).build();
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
//        if (StringUtils.isBlank(sid)) {
//            MultiValueMap<String, HttpCookie> cookies = request.getCookies();
//            if (!CollectionUtils.isEmpty(cookies)) {
//                HttpCookie httpCookie = cookies.getFirst(sessionHeader);
//                if (Objects.nonNull(httpCookie)) {
//                    byte[] encodedCookieBytes = Base64.getDecoder().decode(httpCookie.getValue());
//                    sid = new String(encodedCookieBytes);
//                } else {
//                    httpCookie = cookies.getFirst("_s");
//                    if (Objects.nonNull(httpCookie)) {
//                        byte[] encodedCookieBytes = Base64.getDecoder().decode(httpCookie.getValue());
//                        sid = new String(encodedCookieBytes);
//                    }
//                }
//            }
//        }
        return sid;
    }

    private void cookieWrite(ServerHttpRequest request, ServerHttpResponse response, String value) {
//        byte[] encodedCookieBytes = Base64.getEncoder().encode(value.getBytes());
//        String baseVal = new String(encodedCookieBytes);
//
//        //https://blog.csdn.net/weixin_44269886/article/details/102459425?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-2&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-2
//        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(sessionHeader, baseVal)
//                //.path(request.getPath().contextPath().value() + "/")
//                .path("/")
//                .maxAge(-1)
//                .httpOnly(true)
//                .sameSite("None")
//                .secure("https".equalsIgnoreCase(request.getURI().getScheme()));
//        response.addCookie(cookieBuilder.build());
//
//        cookieBuilder = ResponseCookie.from("_s", baseVal)
//                .path("/")
//                .maxAge(-1)
//                .httpOnly(true)
//                .sameSite("None")
//                .secure("https".equalsIgnoreCase(request.getURI().getScheme()));
//        response.addCookie(cookieBuilder.build());
    }

    class CookieBuilder {
        private String key;
        private String value;
        /**
         * 设置cookie的有效期.
         * 如果cookie超过date所表示的日期时，cookie将失效。 如果没有设置这个选项，那么cookie将在浏览器关闭时失效。
         * date是格林威治时间(GMT)
         */
        private String expires;
        /**
         * 临时cookie(没有expires参数的cookie)不能带有domain选项。
         */
        private String domain;
        private String path;
        /**
         * 表示cookie只能被发送到http服务器
         */
        private String secure;
        /**
         * 表示cookie不能被客户端脚本获取到
         */
        private String httponly;

        public CookieBuilder setKey(String key) {
            this.key = key;
            return this;
        }

        public CookieBuilder setValue(String value) {
            this.value = value;
            return this;
        }

        public CookieBuilder setMaxAge(long ms) {
            //cookie的过期日期为GMT格式的时间。
            Date date = new Date(System.currentTimeMillis() + ms);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            this.expires = sdf.format(date);
            return this;
        }

        public CookieBuilder setDomain(String domain) {
            this.domain = domain;
            return this;
        }

        public CookieBuilder setPath(String path) {
            this.path = path;
            return this;
        }

        public CookieBuilder setSecure(String secure) {
            this.secure = secure;
            return this;
        }

        public CookieBuilder setHttponly(String httponly) {
            this.httponly = httponly;
            return this;
        }

        public String build() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.key);
            sb.append("=");
            sb.append(this.value);
            sb.append(";");
            if (null != this.expires) {
                sb.append("expires=");
                sb.append(this.expires);
                sb.append(";");
            }
            if (null != this.domain) {
                sb.append("domain=");
                sb.append(this.domain);
                sb.append(";");
            }
            if (null != this.path) {
                sb.append("path=");
                sb.append(this.path);
                sb.append(";");
            }
            if (null != this.secure) {
                sb.append("secure=");
                sb.append(this.secure);
                sb.append(";");
            }
            if (null != this.httponly) {
                sb.append("httponly=");
                sb.append(this.httponly);
                sb.append(";");
            }
            return sb.toString();
        }
    }
}