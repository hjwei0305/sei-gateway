package com.ecmp.apigateway.web;

import com.ecmp.apigateway.dao.GatewayApplicationDao;
import com.ecmp.apigateway.exception.ObjectNotFoundException;
import com.ecmp.apigateway.exception.RequestAccessedException;
import com.ecmp.apigateway.model.GatewayApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author:wangdayin
 * @date:2018/4/24
 * @remark:
 */
@Controller
public class DemoController {
    @Autowired
    private GatewayApplicationDao gatewayApplicationDao;

    @RequestMapping("/test")
    @ResponseBody
    public Object test() {
//        throw new ObjectNotFoundException();
//        return "success";
        throw new RequestAccessedException();
    }

    @RequestMapping("find")
    @ResponseBody
    public Object find() {
        List<GatewayApplication> allByDeletedFalse = this.gatewayApplicationDao.findByDeletedFalse();
        return allByDeletedFalse;
    }

    @RequestMapping("/hello")
    public String index() {
        return "hello";
    }
}
