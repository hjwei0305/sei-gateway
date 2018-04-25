package com.ecmp.apigateway.web;

import com.ecmp.apigateway.dao.GatewayApiRouterDao;
import com.ecmp.apigateway.model.GatewayApiRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 控制层：网关-路由配置
 */
@Controller
@RequestMapping(value="/api_router")
public class GatewayApiRouterController {
    @Autowired
    private GatewayApiRouterDao gatewayApiRouterDao;

    @ResponseBody
    @RequestMapping("find")
    public Object find() {
        List<GatewayApiRouter> allByDeletedFalse = this.gatewayApiRouterDao.findAllByDeletedFalse();
        return allByDeletedFalse;
    }
}
