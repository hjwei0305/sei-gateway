package com.ecmp.apigateway.exception;

import com.ecmp.apigateway.exception.message.MessageRuntimeException;

/**
 * @author: hejun
 * @date: 2018/4/26
 * @remark: 路由刷新请求访问异常
 */
public class RequestRouterException extends MessageRuntimeException {
    public RequestRouterException() {
        super("router.request.access.error");
    }
}
