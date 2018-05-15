package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.dao.GatewayApiRouterDao;
import com.ecmp.apigateway.exception.InvokeRouteFailException;
import com.ecmp.apigateway.exception.ObjectNotFoundException;
import com.ecmp.apigateway.exception.RequestParamNullException;
import com.ecmp.apigateway.model.GatewayApiRouter;
import com.ecmp.apigateway.service.IGatewayApiRouterClient;
import com.ecmp.apigateway.service.IGatewayApiRouterService;
import com.ecmp.apigateway.utils.EntityUtils;
import com.ecmp.apigateway.utils.ToolUtils;
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
    @Autowired
    private IGatewayApiRouterClient gatewayApiRouterClient;

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
                gatewayApiRouter.setId(apiRouterOnly.getId()); //*ID值保持一致
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
        List<GatewayApiRouter> gatewayApiRouters = gatewayApiRouterDao.findByDeletedFalseAndServiceIdIn(ToolUtils.id2List(serviceId));
        if (ToolUtils.isEmpty(gatewayApiRouters)) {
            throw new ObjectNotFoundException();
        } else {
            gatewayApiRouters.forEach(gatewayApiRouter -> {
                gatewayApiRouter.setEnabled(enable);
            });
            gatewayApiRouterDao.save(gatewayApiRouters);
        }
    }

    @Override
    public List<GatewayApiRouter> findByServiceId(String serviceId) {
        return gatewayApiRouterDao.findByDeletedFalseAndServiceId(serviceId);
    }

    @Override
    public Object refresh() {
        Object o = gatewayApiRouterClient.refresh();
        if (ToolUtils.isEmpty(o)) {
            throw new InvokeRouteFailException();
        } else {
            return o;
        }
    }
}
