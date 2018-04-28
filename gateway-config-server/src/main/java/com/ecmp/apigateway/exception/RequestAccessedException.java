package com.ecmp.apigateway.exception;

/**
 * @author: hejun
 * @date: 2018/4/26
 * @remark: API请求访问异常
 */
public class RequestAccessedException extends MessageRuntimeException {
    public RequestAccessedException() {
        super("api.request.access.error");
    }
}
