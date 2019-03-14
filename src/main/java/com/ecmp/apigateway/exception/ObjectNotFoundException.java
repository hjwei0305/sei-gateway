package com.ecmp.apigateway.exception;

import com.ecmp.apigateway.exception.message.MessageRuntimeException;

/**
 * @author:wangdayin
 * @date:2018/4/25
 * @remark: 对象未找到异常
 */
public class ObjectNotFoundException extends MessageRuntimeException {
    public ObjectNotFoundException() {
        super("object.not.found.error");
    }
}
