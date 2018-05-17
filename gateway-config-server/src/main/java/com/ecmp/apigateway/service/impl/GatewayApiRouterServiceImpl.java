package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.dao.GatewayApiRouterDao;
import com.ecmp.apigateway.exception.ObjectNotFoundException;
import com.ecmp.apigateway.exception.RequestParamNullException;
import com.ecmp.apigateway.model.GatewayApiRouter;
import com.ecmp.apigateway.model.GatewayApiService;
import com.ecmp.apigateway.service.IGatewayApiRouterService;
import com.ecmp.apigateway.utils.EntityUtils;
import com.ecmp.apigateway.utils.ToolUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/25
 * @remark: 路由配置-业务层实现
 */
@Service
@Transactional
public class GatewayApiRouterServiceImpl implements IGatewayApiRouterService {

    public final static Logger logger = LoggerFactory.getLogger(GatewayApiRouterServiceImpl.class);
    @Autowired
    private GatewayApiRouterDao gatewayApiRouterDao;

    @Value("${gateway.route.service.url}")
    private String gateWayUrl;

    @Value("${gateway.route.service.path}")
    private String gateWayPath;

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
            gatewayApiRouters.forEach(gatewayApiRouter -> gatewayApiRouter.setEnabled(enable));
            gatewayApiRouterDao.save(gatewayApiRouters);
        }
    }

    @Override
    public List<GatewayApiRouter> findByServiceId(String serviceId) {
        return gatewayApiRouterDao.findByDeletedFalseAndServiceId(serviceId);
    }

    @Override
    public Object refresh() {
        HttpGet get =new HttpGet(gateWayUrl+gateWayPath);
        try(CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response =httpClient.execute(get);){
            return org.apache.http.util.EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            logger.error("refresh error",e);
        }
        return null;
    }

    @Override
    public void saveRouterByService(GatewayApiService gatewayApiService) {
        String appUrl = gatewayApiService.getServiceAppUrl();
        GatewayApiRouter gatewayApiRouter = new GatewayApiRouter();
        gatewayApiRouter.setEnabled(true);
        String routeKey = getRouteKey(appUrl);
        gatewayApiRouter.setRouteKey(routeKey);
        gatewayApiRouter.setUrl(appUrl);
        gatewayApiRouter.setPath(ToolUtils.key2Path(gatewayApiRouter.getRouteKey()));
        gatewayApiRouter.setRetryAble(false);
        gatewayApiRouter.setStripPrefix(true);
        gatewayApiRouter.setServiceId(gatewayApiService.getId());
        gatewayApiRouterDao.save(gatewayApiRouter);
    }

    /**
     * 根据配置中心的appUrl获取路由前缀 ，example：/basic-service/
     *
     * @param appUrl
     * @return
     */
    private String getRouteKey(String appUrl) {
        //简单正则匹配ip地址
        return appUrl.replaceAll("http://\\d*\\.\\d*\\.\\d*\\.\\d*:\\d*","");
    }
}
