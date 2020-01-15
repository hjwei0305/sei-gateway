package com.ecmp.apigateway.entity;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author:wangdayin
 * @date:2018/4/24
 * @remark: 网关-接口实体
 * @update:liusonglin 新增部分控制字段
 */
@Entity
@Table(name = "gateway_interface")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class GatewayInterface {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    //接口名称
    @Column(name = "interface_name", nullable = false, length = 100)
    private String interfaceName;
    //接口说明
    @Column(name = "interface_remark", length = 200)
    private String interfaceRemark;
    //接口uri地址
    @Column(name = "interface_uri", nullable = false, length = 150)
    private String interfaceURI;
    //应用code-关联字段
    @Column(name = "application_code", nullable = false, length = 64)
    private String applicationCode;

    /**
     * 会话token验证
     */
    @Column(name = "validate_token", nullable = false)
    private Boolean validateToken = Boolean.FALSE;

    //是否删除--true：已删除；false：未删除
    @Column(name = "deleted", nullable = false)
    private boolean deleted = Boolean.FALSE;

    @Column(name="created_time")
    private String createdTime;

    @Column(name = "updated_time")
    private String updatedTime;

    @Column(name = "creator")
    private String creator;

    @Column(name = "editor")
    private String editor;



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

    public Boolean getValidateToken() {
        return validateToken;
    }

    public void setValidateToken(Boolean validateToken) {
        this.validateToken = validateToken;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "GatewayInterface{" +
                "id='" + id + '\'' +
                ", interfaceName='" + interfaceName + '\'' +
                ", interfaceRemark='" + interfaceRemark + '\'' +
                ", interfaceURI='" + interfaceURI + '\'' +
                ", applicationCode='" + applicationCode + '\'' +
                ", validateToken=" + validateToken +
                ", deleted=" + deleted +
                '}';
    }
}
