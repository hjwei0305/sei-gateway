package com.ecmp.apigateway.zuul.web;

import com.ecmp.apigateway.apigateway.RefreshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 路由配置-控制层跳转
 */
@Controller
public class RouteController {

//    @Autowired
//    private RefreshService refreshService;
//
//    @Autowired
//    private ZuulHandlerMapping zuulHandlerMapping;

    @Autowired
    private RefreshService refreshRoute;

    @ResponseBody
    @RequestMapping("/router/refresh")
    public Object refresh() {
        refreshRoute.refresh();
        return "success";
    }

//    @ResponseBody
//    @RequestMapping("/router/findAll")
//    public Object findAll() {
//        return zuulHandlerMapping.getHandlerMap();
//    }
}
