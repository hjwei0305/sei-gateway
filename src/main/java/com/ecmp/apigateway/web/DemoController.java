package com.ecmp.apigateway.web;

import com.ecmp.apigateway.dao.GatewayApplicationDao;
import com.ecmp.apigateway.model.GatewayApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author:wangdayin
 * @date:2018/4/24
 * @remark:
 */
@RestController
public class DemoController {
    @Autowired
    private GatewayApplicationDao gatewayApplicationDao;

    @RequestMapping("/test")
    public String test() {
        return "success";
    }

    @RequestMapping("find")
    public Object find() {
        List<GatewayApplication> allByDeletedFalse = this.gatewayApplicationDao.findAllByDeletedFalse();
        return allByDeletedFalse;
    }
}
