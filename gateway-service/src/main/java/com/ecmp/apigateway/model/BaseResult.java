package com.ecmp.apigateway.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * usage:返回基础类
 * <p>
 * </p>
 * User:zhengxin; Date:2018/6/13;ProjectName:unionpay;
 */
@ToString
@Getter
@Setter
public class BaseResult<T> {
    public BaseResult(){

    }

    public BaseResult(String code,String msg){
        this.success = false;
        this.code = code;
        this.msg = msg;
    }

    public BaseResult(T t){
        this.success = true;
        this.data = t;
    }

    public BaseResult(Exception ex){
        this.code = "FAILED";
        this.msg = ex.getMessage();
        this.success = false;
    }

    private boolean success = false;

    private String msg;

    private String code;

    private T data;

    public void fillWithData(T t){
        this.success = true;
        this.data = t;
        this.setMsg("成功");
        this.setCode("SUCCESS");
    }

    public void fillWithException(Exception ex){
        this.code = "FAILED";
        this.msg = ex.getMessage();
        this.success = false;
    }

}
