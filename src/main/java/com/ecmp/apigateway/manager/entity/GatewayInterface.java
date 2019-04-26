package com.ecmp.apigateway.manager.entity;

import com.ecmp.apigateway.enums.InterfaceProtocolEnum;
import com.ecmp.core.entity.BaseEntity;

import javax.persistence.*;

/**
 * @author:wangdayin
 * @date:2018/4/24
 * @remark: 网关-接口实体
 * @update:liusonglin 新增部分控制字段
 */
@Entity
@Table(name = "gateway_interface")
public class GatewayInterface extends BaseEntity {
    //接口名称
    @Column(name = "interface_name", nullable = false, length = 100)
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
    /**
     * 会话token验证
     */
    @Column(name = "validate_token", nullable = false)
    private Boolean validateToken = Boolean.TRUE;

    //版本号
    @Column(name = "version", nullable = false)
    @Version
    private int version = 0;
    //是否删除--true：已删除；false：未删除
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

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

    public Boolean getValidateToken() {
        return validateToken;
    }

    public void setValidateToken(Boolean validateToken) {
        this.validateToken = validateToken;
    }

    public GatewayApiService getGatewayApiService() {
        return gatewayApiService;
    }

    public void setGatewayApiService(GatewayApiService gatewayApiService) {
        this.gatewayApiService = gatewayApiService;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
