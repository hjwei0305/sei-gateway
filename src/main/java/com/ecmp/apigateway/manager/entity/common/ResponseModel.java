package com.ecmp.apigateway.manager.entity.common;

import com.ecmp.apigateway.utils.ResourceBundleUtil;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.function.Consumer;

/**
 * @author:wangdayin
 * @date:2018/4/25
 * @remark:
 */
public class ResponseModel<T> implements Serializable {

    public static final int STATUS_SUCCESS = 200;
    public static final int STATUS_ERROR = 500;
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_ACCESS_UNAUTHORIZED = 401;
    public static final int STATUS_ACCESS_FORBIDDEN = 403;


    private Date timestamp = Date.from(Instant.now());
    private int status;
    private String message;
    private T data;

    private ResponseModel(int status, String messageCode, T data) {
        this.status = status;
        this.message = ResourceBundleUtil.getString(messageCode);
        this.data = data;
    }

    private ResponseModel() {
    }

    public static <T> ResponseModel<T> SUCCESS() {
        return SUCCESS("message.success.default", null);
    }

    public static <T> ResponseModel<T> SUCCESS(String messageCode) {
        return SUCCESS(messageCode, null);
    }

    public static <T> ResponseModel<T> SUCCESS(T data) {
        return SUCCESS("message.success.default", data);
    }

    public static <T> ResponseModel<T> SUCCESS(String messageCode, T data) {
        return new ResponseModel<>(STATUS_SUCCESS, messageCode, data);
    }

    public static <T> ResponseModel<T> ERROR() {
        return ERROR("error.internal.error");
    }

    public static <T> ResponseModel<T> ERROR(String messageCode) {
        return ERROR(STATUS_ERROR, messageCode, null);
    }

    public static <T> ResponseModel<T> ERROR(String messageCode, T data) {
        return ERROR(STATUS_ERROR, messageCode, data);
    }

    public static <T> ResponseModel<T> ERROR(int status, String message) {
        ResponseModel<T> model = new ResponseModel<>();
        model.status = status;
        model.message = message;
        return model;
    }

    private static <T> ResponseModel<T> ERROR(int status, String messageCode, T data) {
        return new ResponseModel<>(status, messageCode, data);
    }

    public static <T> ResponseModel<T> NOT_FOUND(String messageCode) {
        return ERROR(STATUS_NOT_FOUND, messageCode, null);
    }

    public static <T> ResponseModel<T> NOT_FOUND() {
        return NOT_FOUND("object.not.found.error");
    }

    public static <T> ResponseModel<T> ACCESS_FORBIDDEN() {
        return ERROR(STATUS_ACCESS_FORBIDDEN, "error.access.forbidden", null);
    }

    public static <T> ResponseModel<T> UNAUTHORIZED(T data) {
        return ERROR(STATUS_ACCESS_UNAUTHORIZED, "error.access.session.invalid", data);
    }


    @Override
    public String toString() {
        return "ResponseModel{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public void ifPresent(Consumer<T> consumer) {
        if (this.data != null) {
            consumer.accept(this.data);
        }
    }

    public void successIfPresent(Consumer<T> consumer) {
        if (this.status == 200 && this.data != null) {
            consumer.accept(this.data);
        }
    }

    public void success(Consumer<T> consumer) {
        if (this.status == 200) {
            consumer.accept(this.data);
        }
    }

    public void fail(Fallback<Integer, String, T> consumer) {
        if (this.status != 200) {
            consumer.accept(this.status, this.message, this.data);
        }
    }

    public void failException() {
        if (this.status != 200) {
            throw new RuntimeException(this.message);
        }
    }

    public <E extends RuntimeException> void failException(E e) {
        if (this.status != 200) {
            throw e;
        }
    }

    @FunctionalInterface
    public interface Fallback<S, M, D> {
        void accept(S status, M message, D data);
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public Date getTimestamp() {
        return timestamp;
    }

}
