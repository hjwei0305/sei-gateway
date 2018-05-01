package com.ecmp.apigateway.web;

import com.ecmp.apigateway.model.GatewayApiRouter;
import com.ecmp.apigateway.model.common.ResponseModel;
import com.ecmp.apigateway.service.IGatewayApiRouterClient;
import com.ecmp.apigateway.service.IGatewayApiRouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 路由配置-控制层跳转
 */
@Controller
@RequestMapping(value = "/gateway_api_router")
public class GatewayApiRouterController {
    @Autowired
    private IGatewayApiRouterService gatewayApiRouterService;

    @Autowired
    private IGatewayApiRouterClient gatewayApiRouterClient;

    /**
     * 新增路由配置
     * @param gatewayApiRouter
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    public Object save(GatewayApiRouter gatewayApiRouter) {
        gatewayApiRouterService.save(gatewayApiRouter);
        return ResponseModel.SUCCESS();
    }

    /**
     * 根据应用服务ID删除
     * @param serviceId 应用服务ID
     * @return
     */
    @ResponseBody
    @RequestMapping("removeByServiceId")
    public Object removeByServiceId(String serviceId) {
        gatewayApiRouterService.removeByServiceId(serviceId);
        gatewayApiRouterClient.refresh();
        return ResponseModel.SUCCESS();
    }

    /**
     * 根据应用服务ID启用
     * @param serviceId 应用服务ID
     * @return
     */
    @ResponseBody
    @RequestMapping("enableByServiceId")
    public Object enableByServiceId(String serviceId) {
        gatewayApiRouterService.enableByServiceId(serviceId);
        gatewayApiRouterClient.refresh();
        return ResponseModel.SUCCESS();
    }

    /**
     * 根据应用服务ID查询
     * @param serviceId 应用服务ID
     * @return
     */
    @ResponseBody
    @RequestMapping("findByServiceId")
    public Object findByServiceId(String serviceId) {
        List<GatewayApiRouter> gatewayApiRouters = gatewayApiRouterService.findByServiceId(serviceId);
        return ResponseModel.SUCCESS(gatewayApiRouters);
    }
}
