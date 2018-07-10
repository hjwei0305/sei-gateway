package com.ecmp.apigateway.model.swagger;

import lombok.Data;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/5/25;ProjectName:guiceexample;
 */
@Data
public class SwaggerBase {
    private String basePath;

    private Info info;

    private String paths;
}
