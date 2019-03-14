package com.ecmp.apigateway.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * usage:查询接口参数
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/23;ProjectName:unionpay;
 */
@Setter
@Getter
@ToString
public class QueryDTO {

    @NotNull(message = "间隔时间 不能为null")
    @Min(value = 5,message = "间隔时间最小值为5分钟")
    @Max(value = 43200,message = "间隔时间最大值为1月（43200分钟）")
    private Long interval;
}

