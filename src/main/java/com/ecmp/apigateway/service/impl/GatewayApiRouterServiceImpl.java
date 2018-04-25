package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.dao.GatewayApiRouterDao;
import com.ecmp.apigateway.model.GatewayApiRouter;
import com.ecmp.apigateway.service.IGatewayApiRouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/25
 * @remark: 网关-路由配置-业务层实现
 */
@Service
@Transactional
public class GatewayApiRouterServiceImpl implements IGatewayApiRouterService {
    @Autowired
    private GatewayApiRouterDao gatewayApiRouterDao;

    @Override
    public GatewayApiRouter save(GatewayApiRouter gatewayApiRouter) {
        gatewayApiRouterDao.save(gatewayApiRouter);
        return gatewayApiRouter;
    }

    @Override
    public int edit(GatewayApiRouter gatewayApiRouter) {
        return 0;
    }

    @Override
    public int removeAll() {
        return gatewayApiRouterDao.removeAll();
    }

    @Override
    public int removeById(String ids) {
        int row = 0;
        String[] idArr = ids.split(",");
        for(String id: idArr){
            row += gatewayApiRouterDao.removeById(id);
        }
        return row;
    }

    @Override
    public Object findAll(String keywords) {
        List<GatewayApiRouter> valueList = gatewayApiRouterDao.findAll(keywords);
        return valueList;
    }

    @Override
    public Object findById(String id) {
        GatewayApiRouter value = gatewayApiRouterDao.findById(id);
        return value;
    }

}
