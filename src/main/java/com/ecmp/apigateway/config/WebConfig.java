package com.ecmp.apigateway.config;

import com.ecmp.context.ContextUtil;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/25;ProjectName:api-gateway;
 */
@Configuration
public class WebConfig implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext servletContext) {
        String staticResourceUrl = ContextUtil.getProperty("static.resource.url");
        servletContext.setAttribute("staticResourceUrl", staticResourceUrl);
    }
}
