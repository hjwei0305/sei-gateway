package com.ecmp.apigateway.globalFilter;

import com.alibaba.fastjson.JSONObject;
import com.ecmp.apigateway.entity.ResultData;
import com.ecmp.apigateway.intergration.AuthFromAccountCenter;
import com.ecmp.apigateway.service.InterfaceService;
import com.ecmp.apigateway.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private InterfaceService interfaceService;
    @Autowired
    private Environment env;
    @Autowired
    private AuthFromAccountCenter authFormAccountCenter;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String uri = request.getPath().toString();
        String sid = request.getHeaders().getFirst(HttpUtils.HEADER_TOKEN);
        ResultData<String> result;
        String internalToken = null;
        if(StringUtils.isBlank(sid)){
            // 没有会话id,先判断接口是否需要认证，不需要认证接口直接请求内部token
            if(shouldFilter(uri)){
                return buildResultHeader(response,"未在请求中找到有效token");
            }else{
                result = authFormAccountCenter.getAnonymousToken();
                if(result.getSuccessful()){
                    internalToken = result.getData();
                }else {
                    return buildResultHeader(response,result.getMessage());
                }
            }
        }else{
            // 有会话id,直接校验会话，如果校验成功,带上会话信息请求，
            // 如果校验失败,先判断接口是否需要认证，不需要认证接口直接请求内部token
            result = authFormAccountCenter.check(sid);
            if(result.getSuccessful()){
                internalToken = result.getData();
            }else {
                if(shouldFilter(uri)){
                    return buildResultHeader(response,result.getMessage());
                }
                result = authFormAccountCenter.getAnonymousToken();
                if(result.getSuccessful()){
                    internalToken = result.getData();
                }else {
                    return buildResultHeader(response,result.getMessage());
                }
            }
        }
        // 如果没有内部token生成,账号服务可能出错
        if(StringUtils.isBlank(internalToken)){
            return buildResultHeader(response,"获取认证信息出错，请联系管理员");
        }
        // 把内部token放入header
        ServerHttpRequest internalRequest = request.mutate().header("internal",internalToken).build();
        ServerWebExchange internalExchange = exchange.mutate().request(internalRequest).build();
        return chain.filter(internalExchange);
    }

    private boolean shouldFilter(String uri) {
        if (StringUtils.containsIgnoreCase(uri, "sso") || StringUtils.containsIgnoreCase(uri, "/monitor/health") ) {
            return false;
        } else {
            String contextPath = env.getProperty("server.servlet.context-path","/api-gateway");
            Boolean checkToken = interfaceService.checkToken(uri.replaceAll(contextPath,""));
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
