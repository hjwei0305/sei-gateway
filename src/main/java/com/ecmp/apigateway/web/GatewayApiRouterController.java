package com.ecmp.apigateway.web;

import com.ecmp.apigateway.service.IGatewayApiRouterService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 网关-路由配置-控制层跳转
 */
@Controller
@RequestMapping(value="/api_router")
public class GatewayApiRouterController {
    @Resource
    private IGatewayApiRouterService gatewayApiRouterService;

    @ResponseBody
    @RequestMapping("save")
    public Object save() {
        return gatewayApiRouterService.save(null);
    }

    @ResponseBody
    @RequestMapping("edit")
    public Object edit() {
        return gatewayApiRouterService.edit(null);
    }

    @ResponseBody
    @RequestMapping("removeAll")
    public Object removeAll() {
        return gatewayApiRouterService.removeAll();
    }

    @ResponseBody
    @RequestMapping("removeById")
    public Object removeById(String ids) {
        return gatewayApiRouterService.removeById(ids);
    }

    @ResponseBody
    @RequestMapping("findAll")
    public Object findAll(String keywords) {
        return gatewayApiRouterService.findAll(keywords);
    }

    @ResponseBody
    @RequestMapping("findById")
    public Object findById(String id) {
        return gatewayApiRouterService.findById(id);
    }
}
