package com.ecmp.apigateway.web;

import com.ecmp.apigateway.dao.GatewayApiServiceDao;
import com.ecmp.apigateway.model.GatewayApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 网关-应用服务-控制层
 */
@Controller
@RequestMapping(value="/api_service")
public class GatewayApiServiceController {
    @Autowired
    private GatewayApiServiceDao gatewayApiServiceDao;

    @ResponseBody
    @RequestMapping("find")
    public Object find() {
        List<GatewayApiService> allByDeletedFalse = this.gatewayApiServiceDao.findAllByDeletedFalse();
        return allByDeletedFalse;
    }
}
