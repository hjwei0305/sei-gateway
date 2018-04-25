package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.dao.GatewayApiServiceDao;
import com.ecmp.apigateway.model.GatewayApiService;
import com.ecmp.apigateway.service.IGatewayApiServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/25
 * @remark: 网关-应用服务-业务层实现
 */
@Service
@Transactional
public class GatewayApiServiceServiceImpl implements IGatewayApiServiceService {
    @Autowired
    private GatewayApiServiceDao gatewayApiServiceDao;

    @Override
    public GatewayApiService save(GatewayApiService gatewayApiService) {
        gatewayApiServiceDao.save(gatewayApiService);
        return gatewayApiService;
    }

    @Override
    public int edit(GatewayApiService gatewayApiService) {
        return 0;
    }

    @Override
    public int removeAll() {
        return gatewayApiServiceDao.removeAll();
    }

    @Override
    public int removeById(String ids) {
        int row = 0;
        String[] idArr = ids.split(",");
        for(String id: idArr){
            row += gatewayApiServiceDao.removeById(id);
        }
        return row;
    }

    @Override
    public Object findAll(String keywords) {
        List<GatewayApiService> valueList = gatewayApiServiceDao.findAll(keywords);
        return valueList;
    }

    @Override
    public Object findById(String id) {
        GatewayApiService value = gatewayApiServiceDao.findById(id);
        return value;
    }

}
