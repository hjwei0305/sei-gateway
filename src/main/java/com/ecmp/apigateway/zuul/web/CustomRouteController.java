package com.ecmp.apigateway.zuul.web;

import com.ecmp.apigateway.zuul.event.RefreshRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 网关-路由配置-控制层跳转
 */
@Controller
@RequestMapping(value="/custom_route")
public class CustomRouteController {
    @Autowired
    RefreshRouteService refreshRouteService;

    @ResponseBody
    @RequestMapping("refreshRoute")
    public Object refreshRoute(){
        refreshRouteService.refreshRoute();
        return "refreshRoute";
    }

    @Autowired
    ZuulHandlerMapping zuulHandlerMapping;

    @ResponseBody
    @RequestMapping("watchNowRoute")
    public Object watchNowRoute(){
        //可以用debug模式看里面具体是什么
        Map<String, Object> handlerMap = zuulHandlerMapping.getHandlerMap();
        return "watchNowRoute";
    }
}
