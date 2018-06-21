package com.ecmp.apigateway.config;

import com.ecmp.apigateway.ZKService;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/21;ProjectName:api-gateway;
 */
@Configuration
public class DataBaseConfig {

    @Autowired
    private ZKService zkService;

    @Value("${ecmp.app.id}")
    private String appId;

    @Bean
    public DataSource dataSource(){
        DataSource dataSource = new DataSource();
        Map<String, String> config = zkService.getConfigMap(appId,"DATASOURCE");
        dataSource.setUrl(config.get("url"));
        dataSource.setUsername(config.get("username"));
        dataSource.setPassword(config.get("password"));
        dataSource.setDriverClassName(config.get("driverClassName"));
        dataSource.setInitialSize(Integer.parseInt(config.get("initialSize")));
        dataSource.setMinIdle(Integer.parseInt(config.get("minIdle")));
        dataSource.setMaxActive(Integer.parseInt(config.get("maxActive")));
        return dataSource;
    }
}
