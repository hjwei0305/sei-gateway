package com.ecmp.apigateway.exception;

import com.ecmp.apigateway.utils.ResourceBundleUtil;

/**
 * @author:wangdayin
 * @date:2018/4/25
 * @remark: 异常统一处理
 */
public class MessageRuntimeException extends RuntimeException {
    private String messageKey;

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