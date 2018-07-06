package com.ecmp.apigateway.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * usage:初始化服务测试
 * <p>
 * </p>
 * User:liusonglin; Date:2018/7/2;ProjectName:api-gateway;
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InitServiceTest {

    @Autowired
    InitService initService;

    @Test
    public void initInterface() throws Exception {
        initService.InitService("B84B4EFB-4465-69AA-6EF0-3D18DD2C2B6D");
    }

    @Test
    public void initService() throws Exception {
    }

}