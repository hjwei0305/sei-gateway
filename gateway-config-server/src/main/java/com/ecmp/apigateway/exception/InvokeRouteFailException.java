package com.ecmp.apigateway.exception;

import com.ecmp.apigateway.exception.message.MessageRuntimeException;

/**
 * @author: hejun
 * @date: 2018/4/26
 * @remark: 调用路由失败异常
 */
public class InvokeRouteFailException extends MessageRuntimeException {
    public InvokeRouteFailException() {
        super("invoke.route.fail.error");
    }
}
