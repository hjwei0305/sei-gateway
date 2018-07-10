package com.ecmp.apigateway.exception;

import com.ecmp.apigateway.exception.message.MessageRuntimeException;

/**
 * @author: hejun
 * @date: 2018/4/26
 * @remark: 调用配置服务失败异常
 */
public class InvokeConfigFailException extends MessageRuntimeException {
    public InvokeConfigFailException() {
        super("invoke.config.fail.error");
    }
}
