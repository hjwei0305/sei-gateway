package com.ecmp.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableSpringConfigured
@ServletComponentScan
@EnableScheduling
public class GatewayConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayConfigServerApplication.class, args);
    }
}
