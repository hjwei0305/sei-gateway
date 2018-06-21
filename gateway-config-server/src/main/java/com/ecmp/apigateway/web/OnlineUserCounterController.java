package com.ecmp.apigateway.web;

import com.ecmp.apigateway.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/20;ProjectName:api-gateway;
 */
@RestController
@RequestMapping("/onlineUser")
public class OnlineUserCounterController {

    @Autowired
    private RedisUtils redisUtils;


    @GetMapping("/counter")
    public long counter(){
        return redisUtils.getCurrentCount();
    }
}
