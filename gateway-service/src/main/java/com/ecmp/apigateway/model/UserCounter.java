package com.ecmp.apigateway.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

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
@Document(indexName = "UserCounter", type = "user", createIndex = false)
public class UserCounter {

    @Id
    private String id;

    /**
     * 名称
     */
    private Integer counter;

    /**
     * 部门占比
     */
    private Map<String,String> sectionProportion;


    private Date addTime;
}
