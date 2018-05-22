package com.ecmp.apigateway.zuul.service;

import com.ecmp.apigateway.dao.GatewayApiServiceDao;
import com.ecmp.apigateway.dao.GatewayInterfaceDao;
import com.ecmp.apigateway.model.GatewayInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * usage:接口服务
 * <p>
 * </p>
 * User:liusonglin; Date:2018/5/18;ProjectName:api-gateway;
 */
@Component
public class InterfaceService {

    @Autowired
    private GatewayInterfaceDao gatewayInterfaceDao;

    public List<GatewayInterface> getInvalidInterface(){
        //todo redis缓存
        return gatewayInterfaceDao.findByIsValidFalse();
    }

}
