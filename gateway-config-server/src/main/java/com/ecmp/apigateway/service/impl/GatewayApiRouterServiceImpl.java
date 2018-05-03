package com.ecmp.apigateway.service.impl;

//import com.ecmp.apigateway.ConfigCenterContext;
import com.ecmp.apigateway.dao.GatewayApiRouterDao;
import com.ecmp.apigateway.exception.ObjectNotFoundException;
import com.ecmp.apigateway.exception.RequestParamNullException;
import com.ecmp.apigateway.model.GatewayApiRouter;
import com.ecmp.apigateway.service.IGatewayApiRouterService;
import com.ecmp.apigateway.utils.EntityUtils;
import com.ecmp.apigateway.utils.ToolUtils;
//import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/25
 * @remark: 路由配置-业务层实现
 */
@Service
@Transactional
public class GatewayApiRouterServiceImpl implements IGatewayApiRouterService {
    @Autowired
    private GatewayApiRouterDao gatewayApiRouterDao;
//    @Autowired
//    private ConfigCenterContext configCenterContext;

    @Override
    public GatewayApiRouter getting(GatewayApiRouter gatewayApiRouter) {
        if (ToolUtils.isEmpty(gatewayApiRouter.getServiceId()) || ToolUtils.isEmpty(gatewayApiRouter.getUrl())) {
            throw new RequestParamNullException();
        } else {
            GatewayApiRouter apiRouterOnly = gatewayApiRouterDao.findByServiceIdAndUrl(gatewayApiRouter.getServiceId(), gatewayApiRouter.getUrl());
            if (ToolUtils.notEmpty(apiRouterOnly)) {
                EntityUtils.resolveAllFieldsSet(gatewayApiRouter, apiRouterOnly);
            }
            return gatewayApiRouter;
        }
    }

    @Override
    public void setting(GatewayApiRouter gatewayApiRouter) {
        if (ToolUtils.isEmpty(gatewayApiRouter.getRouteKey()) || ToolUtils.isEmpty(gatewayApiRouter.getServiceId()) || ToolUtils.isEmpty(gatewayApiRouter.getUrl())) {
            throw new RequestParamNullException();
        } else {
            GatewayApiRouter apiRouterOnly = gatewayApiRouterDao.findByServiceIdAndUrl(gatewayApiRouter.getServiceId(), gatewayApiRouter.getUrl());
            if (ToolUtils.notEmpty(apiRouterOnly)) {
                EntityUtils.resolveAllFieldsSet(gatewayApiRouter, apiRouterOnly);
                gatewayApiRouter.setId(apiRouterOnly.getId());
            }
            gatewayApiRouter.setPath(ToolUtils.key2Path(gatewayApiRouter.getRouteKey()));
            gatewayApiRouterDao.save(gatewayApiRouter);
        }
    }

    @Override
    public void removeByServiceId(String serviceId) {
        List<GatewayApiRouter> gatewayApiRouters = gatewayApiRouterDao.findByDeletedFalseAndServiceIdIn(ToolUtils.id2List(serviceId));
        if (ToolUtils.notEmpty(gatewayApiRouters)) {
            gatewayApiRouters.forEach(gatewayApiRouter -> {
                gatewayApiRouter.setDeleted(true);
                gatewayApiRouter.setEnabled(false);
            });
            gatewayApiRouterDao.save(gatewayApiRouters);
        }
    }

    @Override
    public void enableByServiceId(String serviceId, boolean enable) {
//        //根据appId和应用代码获取应用服务地址
//        //todo 这个BASIC_API应该是GatewayApiRouter中的一个属性或作为参数传进来
//        String apiUrl = configCenterContext.getZookeeperData(serviceId, "BASIC_API");
//        if (StringUtils.isBlank(apiUrl)) {
//            //todo 空处理
//        }
        List<GatewayApiRouter> gatewayApiRouters = gatewayApiRouterDao.findByDeletedFalseAndServiceIdIn(ToolUtils.id2List(serviceId));
        if (ToolUtils.isEmpty(gatewayApiRouters)) {
            throw new ObjectNotFoundException();
        } else {
            gatewayApiRouters.forEach(gatewayApiRouter -> {
                gatewayApiRouter.setEnabled(enable);
//                gatewayApiRouter.setUrl(apiUrl);
            });
            gatewayApiRouterDao.save(gatewayApiRouters);
        }
    }

    @Override
    public List<GatewayApiRouter> findByServiceId(String serviceId) {
        return gatewayApiRouterDao.findByDeletedFalseAndServiceId(serviceId);
    }
}
