package com.ecmp.apigateway.config;

import com.ecmp.apigateway.ZKService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Map;


/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/21;ProjectName:api-gateway;
 */
@Configuration
@Slf4j
public class DataBaseConfig {

    @Autowired
    private ZKService zkService;

    @Value("${ecmp.app.id}")
    private String appId;

    @Bean
    public DataSource dataSource(){
        Map<String, String> config = zkService.getConfigMap(appId,"DATASOURCE");
        log.info("database config is {}",config);

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(config.get("url"));
        hikariConfig.setUsername(config.get("username"));
        hikariConfig.setPassword(config.get("password"));
        hikariConfig.setDriverClassName(config.get("driverClassName"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(config.get("maxActive")));
        hikariConfig.setMinimumIdle(Integer.parseInt(config.get("initialSize")));
        return new HikariDataSource(hikariConfig);
    }
}
