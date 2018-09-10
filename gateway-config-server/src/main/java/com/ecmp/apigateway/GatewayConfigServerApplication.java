package com.ecmp.apigateway;

import com.ecmp.util.JwtTokenUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableSpringConfigured
@ServletComponentScan
@EnableScheduling
@EnableZuulProxy
public class GatewayConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayConfigServerApplication.class, args);
    }


    @Bean
    public JwtTokenUtil jwtTokenUtil() {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        //TODO 设置超时时间
        jwtTokenUtil.setJwtExpiration(3600 * 10);
        return jwtTokenUtil;
    }
}
