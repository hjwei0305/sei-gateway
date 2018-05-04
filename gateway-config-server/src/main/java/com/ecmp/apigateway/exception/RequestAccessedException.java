package com.ecmp.apigateway.exception;

/**
 * @author: hejun
 * @date: 2018/4/26
 * @remark: API配置中心请求访问异常
 */
public class RequestAccessedException extends MessageRuntimeException {
    public RequestAccessedException() {
        super("config.request.access.error");
    }
}
