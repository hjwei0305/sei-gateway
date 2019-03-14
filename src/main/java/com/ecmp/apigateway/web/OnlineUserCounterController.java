package com.ecmp.apigateway.web;

import com.ecmp.apigateway.entity.BaseResult;
import com.ecmp.apigateway.entity.QueryDTO;
import com.ecmp.apigateway.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

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
    public BaseResult<Long> counter() {
        return new BaseResult<>(redisUtils.getCurrentCount());
    }


    @GetMapping("/rangCounter")
    public BaseResult<Map<Date, Double>> rangCounter(@Validated QueryDTO queryDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new BaseResult<>("params",
                    bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            return new BaseResult<>();
        } catch (Exception ex) {
            return new BaseResult<>(ex);
        }


    }
}
