package com.ecmp.apigateway.web;

import com.ecmp.apigateway.dao.GatewayRouterDao;
import com.ecmp.apigateway.model.GatewayRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 控制层：网关-路由
 */
@Controller
@RequestMapping(value="/router")
public class GatewayRouterController {
    @Autowired
    private GatewayRouterDao gatewayRouterDao;

    @ResponseBody
    @RequestMapping("find")
    public Object find() {
        List<GatewayRouter> allByDeletedFalse = this.gatewayRouterDao.findAllByDeletedFalse();
        return allByDeletedFalse;
    }
}
