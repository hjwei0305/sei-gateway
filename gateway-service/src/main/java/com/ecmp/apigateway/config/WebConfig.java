package com.ecmp.apigateway.config;

import com.ecmp.apigateway.ZKService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/25;ProjectName:api-gateway;
 */
@Configuration
public class WebConfig implements ServletContextInitializer {


    @Autowired
    private ZKService zkService;

    @Value("${ecmp.app.id}")
    private String appId;

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        String staticResourceUrl = zkService.getZookeeperData(appId,"static.resource.url");
        servletContext.setAttribute("staticResourceUrl",staticResourceUrl);
    }
}
