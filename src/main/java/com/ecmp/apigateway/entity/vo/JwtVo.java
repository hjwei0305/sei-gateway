package com.ecmp.apigateway.entity.vo;

/**
 * usage:要求客户端jwt编码格式，其中val是通过rsa私钥加密的信息，uri为当前jwt需要请求的地址
 * <p>
 * </p>
 * User:liusonglin; Date:2018/5/22;ProjectName:api-gateway;
 */
public class JwtVo {

    private String appId;

    private String val;

    private String key;

    private String uri;


    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
