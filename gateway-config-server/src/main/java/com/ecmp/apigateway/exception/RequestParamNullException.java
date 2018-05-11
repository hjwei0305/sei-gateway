package com.ecmp.apigateway.exception;

import com.ecmp.apigateway.exception.message.MessageRuntimeException;

/**
 * @author:wangdayin
 * @date:2018/4/25
 * @remark: 请求参数为空异常
 */
public class RequestParamNullException extends MessageRuntimeException {
    public RequestParamNullException() {
        super("request.param.null.error");
    }
}
