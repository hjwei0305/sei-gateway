package com.ecmp.routerserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

@SpringBootApplication
@EnableSpringConfigured
@ServletComponentScan
@EnableZuulProxy
public class RouterServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RouterServerApplication.class, args);
    }
}
