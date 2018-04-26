package com.ecmp.routerserver.zuul.web;

import com.ecmp.routerserver.zuul.event.RefreshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 路由配置-控制层跳转
 */
@Controller
@RequestMapping(value = "/route_service")
public class RouteController {
    @Autowired
    RefreshService refreshService;

    @ResponseBody
    @RequestMapping("refreshRoute")
    public Object refreshRoute() {
        refreshService.refreshRoute();
        return "refreshRoute";
    }

    @Autowired
    ZuulHandlerMapping zuulHandlerMapping;

    @ResponseBody
    @RequestMapping("watchNowRoute")
    public Object watchNowRoute() {
        //可以用debug模式看里面具体是什么
        Map<String, Object> handlerMap = zuulHandlerMapping.getHandlerMap();
        return handlerMap;
    }
}
