package com.ecmp.apigateway.entity.common;

import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author:wangdayin
 * @date:2018/4/24
 * @remark: 公用基类
 */
@MappedSuperclass
public class Domain implements Serializable {
    //主键ID
    @Id
    private String id = UUID.randomUUID().toString();
    //创建时间
    @Column(name = "created_time", nullable = false, updatable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdTime = new Date();
    //更新时间
    @Column(name = "updated_time", nullable = false, updatable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedTime = new Date();
    //创建者
    @Column(name = "creator")
    private String creator;
    //更新者
    @Column(name = "editor")
    private String editor;
    //版本号
    @Column(name = "version", nullable = false)
    @Version
    private int version = 0;
    //是否删除--true：已删除；false：未删除
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
