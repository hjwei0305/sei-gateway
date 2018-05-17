package com.ecmp.apigateway.zuul.web;

import com.ecmp.apigateway.zuul.event.RefreshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 路由配置-控制层跳转
 */
@Controller
@RequestMapping(value = "/gateway_route_service")
public class RouteController {
    @Autowired
    RefreshService refreshService;
    @Autowired
    ZuulHandlerMapping zuulHandlerMapping;

    @ResponseBody
    @RequestMapping("/router/refresh")
    public Object refresh() {
        refreshService.refreshRoute();
        return "success";
    }

    @ResponseBody
    @RequestMapping("/router/findAll")
    public Object findAll() {
        return zuulHandlerMapping.getHandlerMap();
    }
}
