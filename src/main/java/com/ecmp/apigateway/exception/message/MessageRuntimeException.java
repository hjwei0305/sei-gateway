package com.ecmp.apigateway.exception.message;

import com.ecmp.apigateway.utils.ResourceBundleUtil;

/**
 * @author:wangdayin
 * @date:2018/4/25
 * @remark: 运行异常统一处理
 */
public class MessageRuntimeException extends RuntimeException {
    private final String messageKey;

    public MessageRuntimeException(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public String getMessage() {
        try {
            return ResourceBundleUtil.getString(messageKey);
        } catch (Exception e) {
            return messageKey;
        }
    }
}
