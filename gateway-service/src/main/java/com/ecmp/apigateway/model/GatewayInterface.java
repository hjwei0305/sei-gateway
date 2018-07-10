package com.ecmp.apigateway.model;

import com.ecmp.apigateway.enums.InterfaceProtocolEnum;
import com.ecmp.apigateway.model.common.Domain;

import javax.persistence.*;

/**
 * @author:wangdayin
 * @date:2018/4/24
 * @remark: 网关-接口实体
 * @update:liusonglin 新增部分控制字段
 */
@Entity
@Table(name = "gateway_interface")
public class GatewayInterface extends Domain {
    //接口名称
    @Column(name = "interface_name", nullable = false, length = 50)
    private String interfaceName;
    //接口说明
    @Column(name = "interface_remark", length = 200)
    private String interfaceRemark;
    //接口协议
    @Column(name = "interface_protocol", nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    private InterfaceProtocolEnum interfaceProtocol;
    //接口uri地址
    @Column(name = "interface_uri", nullable = false, length = 150)
    private String interfaceURI;
    //应用code-关联字段
    @Column(name = "application_code", nullable = false, length = 64)
    private String applicationCode;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "application_code", referencedColumnName = "application_code", insertable = false, updatable = false)
    private GatewayApiService gatewayApiService;
    /**
     * 是否可用
     */
    @Column(name = "is_valid", nullable = false)
    private boolean isValid;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceRemark() {
        return interfaceRemark;
    }

    public void setInterfaceRemark(String interfaceRemark) {
        this.interfaceRemark = interfaceRemark;
    }

    public InterfaceProtocolEnum getInterfaceProtocol() {
        return interfaceProtocol;
    }

    public void setInterfaceProtocol(InterfaceProtocolEnum interfaceProtocol) {
        this.interfaceProtocol = interfaceProtocol;
    }

    public String getInterfaceURI() {
        return interfaceURI;
    }

    public void setInterfaceURI(String interfaceURI) {
        this.interfaceURI = interfaceURI;
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public GatewayApiService getGatewayApiService() {
        return gatewayApiService;
    }

    public void setGatewayApiService(GatewayApiService gatewayApiService) {
        this.gatewayApiService = gatewayApiService;
    }


    @Override
    public String toString() {
        return "GatewayInterface{" +
                "interfaceName='" + interfaceName + '\'' +
                ", interfaceRemark='" + interfaceRemark + '\'' +
                ", interfaceProtocol=" + interfaceProtocol +
                ", interfaceURI='" + interfaceURI + '\'' +
                ", applicationCode='" + applicationCode + '\'' +
                ", gatewayApiService=" + gatewayApiService +
                ", isValid=" + isValid +
                '}';
    }
}
