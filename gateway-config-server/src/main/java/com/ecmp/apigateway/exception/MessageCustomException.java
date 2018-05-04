package com.ecmp.apigateway.exception;

/**
 * @author: hejun
 * @date: 2018/5/4
 * @remark: 自定义消息异常
 */
public class MessageCustomException extends RuntimeException {
    private String message;

    public MessageCustomException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
