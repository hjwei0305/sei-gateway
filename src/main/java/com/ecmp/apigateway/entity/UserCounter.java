package com.ecmp.apigateway.entity;

import com.ecmp.core.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.Map;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/22;ProjectName:api-gateway;
 */
@Getter
@Setter
@ToString
public class UserCounter extends BaseEntity {

    @Id
    private String id;

    /**
     * 名称
     */
    private Integer counter;

    /**
     * 部门占比
     */
    private Map<String, String> sectionProportion;


    private Date addTime;
}
