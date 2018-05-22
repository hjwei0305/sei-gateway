package com.ecmp.apigateway.model.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * usage:要求客户端jwt编码格式，其中val是通过rsa私钥加密的信息，uri为当前jwt需要请求的地址
 * <p>
 * </p>
 * User:liusonglin; Date:2018/5/22;ProjectName:api-gateway;
 */
@Setter
@Getter
@ToString
public class JwtVo {

    private String appId;

    private String val;

    private String key;

    private String uri;
}
