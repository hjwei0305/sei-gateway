package com.ecmp.apigateway.exception;

/**
 * @author: hejun
 * @date: 2018/4/26
 * @remark: 请求访问异常
 */
public class RequestAccessException extends MessageRuntimeException {
    public RequestAccessException() {
        super("request.access.fallback.error");
    }
}
