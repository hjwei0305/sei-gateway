package com.changhong.sei.apigateway.globalFilter;

import com.changhong.sei.apigateway.intergration.AuthFromAccountCenter;
import com.changhong.sei.apigateway.service.InterfaceService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Autowired
    private InterfaceService interfaceService;
    @Autowired
    private Environment env;
    @Autowired
    private AuthFromAccountCenter authFormAccountCenter;

    @Value("${internal.header}")
    public String internalHeader;
    @Value("${session.header}")
    public String sessionHeader;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String uri = request.getPath().toString();
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
                    return buildResultHeader(response, "未在请求中找到有效token");
                } else {
                    result = authFormAccountCenter.getAnonymousToken();
                    if (result.successful()) {
                        internalToken = result.getData();
                    } else {
                        return buildResultHeader(response, result.getMessage());
                    }
                }
            }
        } else {
            // 有会话id,直接校验会话，如果校验成功,带上会话信息请求，
            // 如果校验失败,先判断接口是否需要认证，不需要认证接口直接请求内部token
            result = authFormAccountCenter.check(sid);
            if (result.successful()) {
                // 设置cookie
                cookieWrite(request, response, sid);

                internalToken = result.getData();
            } else {
                if (shouldFilter(uri)) {
                    return buildResultHeader(response, result.getMessage());
                }
                result = authFormAccountCenter.getAnonymousToken();
                if (result.successful()) {
                    internalToken = result.getData();
                } else {
                    return buildResultHeader(response, result.getMessage());
                }
            }
        }
        // 如果没有内部token生成,账号服务可能出错
        if (StringUtils.isBlank(internalToken)) {
            return buildResultHeader(response, "获取认证信息出错，请联系管理员");
        }
        // 把内部token放入header
//        System.out.println("内部token: "+internalHeader+" = " + internalToken);
        ServerHttpRequest internalRequest = request.mutate().header(internalHeader, internalToken).build();
        ServerWebExchange internalExchange = exchange.mutate().request(internalRequest).build();
        return chain.filter(internalExchange);
    }

    private boolean shouldFilter(String uri) {
        if (StringUtils.containsIgnoreCase(uri, "sso") || StringUtils.containsIgnoreCase(uri, "/monitor/health")) {
            return false;
        } else {
            String contextPath = env.getProperty("server.servlet.context-path", "/api-gateway");
            Boolean checkToken = interfaceService.checkToken(uri.replaceAll(contextPath, ""));
            log.info("uri: {}, 是否需要Token检查: {}", uri, checkToken);
            return checkToken;
        }
    }

    public Mono<Void> buildResultHeader(ServerHttpResponse response, String msg) {
        ResultData resultData = ResultData.fail(msg);
        byte[] bits = JsonUtils.toJson(resultData).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().set("Content-Type", "text/plain;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private String getSid(ServerHttpRequest request) {
        String sid = request.getHeaders().getFirst(sessionHeader);
        if (StringUtils.isBlank(sid)) {
            MultiValueMap<String, HttpCookie> cookies = request.getCookies();
            if (!CollectionUtils.isEmpty(cookies)) {
                HttpCookie httpCookie = cookies.getFirst(sessionHeader);
                if (Objects.nonNull(httpCookie)) {
                    byte[] encodedCookieBytes = Base64.getDecoder().decode(httpCookie.getValue());
                    sid = new String(encodedCookieBytes);
                } else {
                    httpCookie = cookies.getFirst("_s");
                    if (Objects.nonNull(httpCookie)) {
                        byte[] encodedCookieBytes = Base64.getDecoder().decode(httpCookie.getValue());
                        sid = new String(encodedCookieBytes);
                    }
                }
            }
        }
        return sid;
    }

    private void cookieWrite(ServerHttpRequest request, ServerHttpResponse response, String value) {
        byte[] encodedCookieBytes = Base64.getEncoder().encode(value.getBytes());
        String baseVal = new String(encodedCookieBytes);

        String cookie = new CookieBuilder().setKey(sessionHeader)
                .setValue(baseVal)
                .setHttponly("true")
                .setPath("/")
                .setSecure(String.valueOf("https".equalsIgnoreCase(request.getURI().getScheme())))
                //https://blog.csdn.net/weixin_44269886/article/details/102459425?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-2&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-2
                //.sameSite("Lax");
                .build();
        response.getHeaders().add("Set-Cookie", cookie);

        cookie = new CookieBuilder().setKey("_s")
                .setValue(baseVal)
                .setHttponly("true")
                .setPath("/")
                .setSecure(String.valueOf("https".equalsIgnoreCase(request.getURI().getScheme())))
                .build();

        response.getHeaders().add("Set-Cookie", cookie);
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
