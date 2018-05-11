package com.ecmp.apigateway.exception.message;

import com.ecmp.apigateway.utils.ResourceBundleUtil;

/**
 * @author: hejun
 * @date: 2018/5/4
 * @remark: 顶层异常统一消息
 */
public class MessageProjectException extends Exception {
    private String messageKey;

    public MessageProjectException(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public String getMessage() {
        try {
            return ResourceBundleUtil.getString(messageKey);
        } catch (Exception e) {
            return messageKey + "：" + e.toString();
        }
    }
}
