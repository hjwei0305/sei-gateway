package com.ecmp.apigateway.web;

import com.ecmp.apigateway.dao.GatewayServiceDao;
import com.ecmp.apigateway.model.GatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 控制层：网关-服务
 */
@Controller
@RequestMapping(value="/service")
public class GatewayServiceController {
    @Autowired
    private GatewayServiceDao gatewayServiceDao;

    @ResponseBody
    @RequestMapping("find")
    public Object find() {
        List<GatewayService> allByDeletedFalse = this.gatewayServiceDao.findAllByDeletedFalse();
        return allByDeletedFalse;
    }
}
