package com.ecmp.apigateway.enums;

/**
 * @author:wangdayin
 * @date:2018/4/24
 * @remark:接口协议枚举
 */
public enum InterfaceProtocolEnum {
    HTTP("HTTP协议"),
    RPC("RPC协议"),
    WEBSERVICE("webservice协议");


    //枚举说明
    private String enumRemark;

    InterfaceProtocolEnum(String enumRemark) {
        this.enumRemark = enumRemark;
    }

    public String getEnumRemark() {
        return enumRemark;
    }
}
