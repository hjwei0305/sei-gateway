package com.changhong.sei.apigateway.enums;

import lombok.Getter;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/20;ProjectName:api-gateway;
 */
public enum RedisEnum {

    ROUTER_SERVICE_PREFIX("ROUTER_SERVICE_PREFIX","路由服务redis前缀"),

    ONLINE_USER_COUNTER("ONLINE_USER_COUNTER","在线用户计数器");

    @Getter
    private String key;

    @Getter
    private String msg;


    RedisEnum(String key,String msg){
        this.key = key;
        this.msg = msg;
    }

}
