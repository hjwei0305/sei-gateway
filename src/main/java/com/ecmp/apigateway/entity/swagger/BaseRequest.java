package com.ecmp.apigateway.entity.swagger;

import lombok.Data;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/5/25;ProjectName:guiceexample;
 */
@Data
public class BaseRequest {
    private String[] tags;

    private String summary;

    private String description;
}
