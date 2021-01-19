package com.changhong.sei.apigateway.filter;

import com.changhong.sei.apigateway.commons.Constants;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 实现功能：Swagger响应体处理,增加基路径
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-01-19 10:49
 */
@Component
public class SwaggerResponseGlobalFilter implements GlobalFilter, Ordered {
    private static final String MATCH_STR = "\"basePath\":\"/api-gateway\"";

    @Override
    public int getOrder() {
        // -1 is response write filter, must be called before that
        return -2;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String uri = request.getPath().toString();
        // 平台全局忽略会话检查的地址.后期可以改为网关的配置读取
        if (StringUtils.contains(uri, Constants.SWAGGER2URL)) {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    if (body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                        return super.writeWith(fluxBody.map(dataBuffer -> {
                            // probably should reuse buffers
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            //释放掉内存
                            DataBufferUtils.release(dataBuffer);
                            String str = new String(content, StandardCharsets.UTF_8);
                            if (str.indexOf(MATCH_STR) > 0) {
                                str = str.replace(MATCH_STR, "\"basePath\":\"" + uri.replaceAll(Constants.SWAGGER2URL, "") + "\"");
                                byte[] uppedContent = str.getBytes();
                                return bufferFactory.wrap(uppedContent);
                            } else {
                                byte[] uppedContent = new String(content, StandardCharsets.UTF_8).getBytes();
                                return bufferFactory.wrap(uppedContent);
                            }
                        }));
                    }
                    // if body is not a flux. never got there.
                    return super.writeWith(body);
                }
            };
            // replace response with decorator
            return chain.filter(exchange.mutate().response(decoratedResponse).build());
        } else {
            return chain.filter(exchange);
        }
    }
}
