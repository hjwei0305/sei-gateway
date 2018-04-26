package com.ecmp.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

@SpringBootApplication
@ServletComponentScan
@EnableSpringConfigured
@EnableFeignClients
@EnableZuulProxy
public class GatewayConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayConfigServerApplication.class, args);
    }
}
