package com.ecmp.apigateway.model.swagger;

import lombok.Data;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/5/25;ProjectName:guiceexample;
 */
@Data
public class ApplicationWithDoc {
    private String appId;

    private String apiDocsUrl;

    private String remark;

    private RuntimeEnvironment runtimeEnvironment;

    private ApplicationModule applicationModule;
}
