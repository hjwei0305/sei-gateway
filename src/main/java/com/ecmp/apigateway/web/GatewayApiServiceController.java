package com.ecmp.apigateway.web;

import com.ecmp.apigateway.service.IGatewayApiServiceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 网关-应用服务-控制层跳转
 */
@Controller
@RequestMapping(value="/api_service")
public class GatewayApiServiceController {
    @Resource
    private IGatewayApiServiceService gatewayApiServiceService;

    @ResponseBody
    @RequestMapping("save")
    public Object save() {
        return gatewayApiServiceService.save(null);
    }

    @ResponseBody
    @RequestMapping("edit")
    public Object edit() {
        return gatewayApiServiceService.edit(null);
    }

    @ResponseBody
    @RequestMapping("removeAll")
    public Object removeAll() {
        return gatewayApiServiceService.removeAll();
    }

    @ResponseBody
    @RequestMapping("removeById")
    public Object removeById(String ids) {
        return gatewayApiServiceService.removeById(ids);
    }

    @ResponseBody
    @RequestMapping("findAll")
    public Object findAll(String keywords) {
        return gatewayApiServiceService.findAll(keywords);
    }

    @ResponseBody
    @RequestMapping("findById")
    public Object findById(String id) {
        return gatewayApiServiceService.findById(id);
    }
}
