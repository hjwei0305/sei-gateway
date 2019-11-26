package com.ecmp.apigateway.apigateway.globalFilter;

import com.alibaba.fastjson.JSONObject;
import com.ecmp.apigateway.apigateway.service.InterfaceService;
import com.ecmp.apigateway.utils.HttpUtils;
import com.ecmp.context.ContextUtil;
import com.ecmp.vo.SessionUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private static final String TOKEN_PREFIX = "Bearer ";

    private static final String REDIS_KEY_JWT = "jwt:";

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private InterfaceService interfaceService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //返回401状态码和提示信息
        if (shouldFilter(request.getURI().getPath())) {
            String uri = request.getPath().toString();
            String token = request.getHeaders().getFirst(HttpUtils.HEADER_TOKEN);
            boolean isToken = true;
            if (StringUtils.isBlank(token)) {
                isToken = false;
                String sid = request.getHeaders().getFirst(HttpUtils.HEADER_SID);
                if (StringUtils.isBlank(sid)) {
                    sid = request.getQueryParams().getFirst(HttpUtils.HEADER_SID);
                    if (StringUtils.isBlank(sid)) {
                        sid = HttpUtils.readCookieValue(request);
                    }
                }

                log.info("Access sid is {}  uri {}", sid, uri);
                if (StringUtils.isNotBlank(sid)) {
                    token = redisTemplate.opsForValue().get(REDIS_KEY_JWT + sid);
                }
            }
            log.info("Access Token is {}  uri {}", token, uri);
            if (StringUtils.isBlank(token)) {
                return buildResultHeader(response,"未在请求中找到有效token");
            }
            if (token.startsWith(TOKEN_PREFIX)) {
                // 截取token
                token = token.substring(TOKEN_PREFIX.length());
            }
            try {
                SessionUser sessionUser = ContextUtil.getSessionUser(token);
                log.info("SessionUser is {}  uri {}", sessionUser, uri);
                if (sessionUser.isAnonymous()) {
                    log.error("非法的token   uri {}", uri);
                    return buildResultHeader(response,"请求中的token是无效或非法的");
                } else {
//                todo 因有部分是通过工具类生成的token，没有放到redis中，故暂时屏蔽该检查，待后续优化
//                String token1 = redisTemplate.opsForValue().get(REDIS_KEY_JWT + sessionUser.getSessionId());
//                if (StringUtils.isBlank(token1) || !StringUtils.equals(token, token1)) {
//                    ctx.setSendZuulResponse(false);
//                    ctx.setResponseStatusCode(ResponseModel.STATUS_ACCESS_UNAUTHORIZED);
//                    log.error("会话过期  uri {}", uri);
//                    ctx.setResponseBody(JsonUtils.toJson(ResponseModel.UNAUTHORIZED("会话过期")));
//                    ctx.set("isSuccess", false);
//                    return null;
//                } else {
                    //header中设置token
                    if (!isToken) {
                        response.getHeaders().add(HttpUtils.HEADER_TOKEN, token);
                    }
//                }
                }
            } catch (Exception ex) {
                log.error("jwt解析失败  URI  {}", uri);
                return buildResultHeader(response,"jwt解析失败");
            }

        }
        return chain.filter(exchange);
    }

    private boolean shouldFilter(String uri) {
        if (StringUtils.containsIgnoreCase(uri, "sso")) {
            return false;
        } else {
            Boolean checkToken = interfaceService.checkToken(uri.replaceAll("/api-gateway",""));
            log.info("uri: {}, 是否需要Token检查: {}", uri, checkToken);
            return checkToken;
        }
    }

    public Mono<Void> buildResultHeader(ServerHttpResponse response,String msg){
        JSONObject message = new JSONObject();
        message.put("status", 500);
        message.put("message", msg);
        byte[] bits = message.toJSONString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
