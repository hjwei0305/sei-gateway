package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.ConfigCenterContextApplication;
import com.ecmp.apigateway.dao.GatewayApiServiceDao;
import com.ecmp.apigateway.exception.InvokeRouteFailException;
import com.ecmp.apigateway.exception.ObjectNotFoundException;
import com.ecmp.apigateway.exception.InvokeConfigFailException;
import com.ecmp.apigateway.exception.RequestParamNullException;
import com.ecmp.apigateway.model.GatewayApiService;
import com.ecmp.apigateway.model.common.SearchParam;
import com.ecmp.apigateway.service.IGatewayApiAppClient;
import com.ecmp.apigateway.service.IGatewayApiRouterClient;
import com.ecmp.apigateway.service.IGatewayApiRouterService;
import com.ecmp.apigateway.service.IGatewayApiServiceService;
import com.ecmp.apigateway.utils.EntityUtils;
import com.ecmp.apigateway.utils.ToolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/25
 * @remark: 应用服务-业务层实现
 */
@Service
@Transactional
public class GatewayApiServiceServiceImpl implements IGatewayApiServiceService {
    @Autowired
    private GatewayApiServiceDao gatewayApiServiceDao;
    @Autowired
    private IGatewayApiRouterService gatewayApiRouterService;
    @Autowired
    private IGatewayApiRouterClient gatewayApiRouterClient;
    @Autowired
    private IGatewayApiAppClient gatewayApiAppClient;
    @Autowired
    private ConfigCenterContextApplication configApplication;

    @Override
    public void save(GatewayApiService gatewayApiService) {
        if (ToolUtils.isEmpty(gatewayApiService.getServiceAppId()) || ToolUtils.isEmpty(gatewayApiService.getApplicationCode())) {
            throw new RequestParamNullException();
        } else {
            gatewayApiServiceDao.save(gatewayApiService);
        }
    }

    @Override
    public void edit(GatewayApiService gatewayApiService) {
        if (ToolUtils.isEmpty(gatewayApiService.getId()) || ToolUtils.isEmpty(gatewayApiService.getServiceAppId()) || ToolUtils.isEmpty(gatewayApiService.getApplicationCode())) {
            throw new RequestParamNullException();
        } else {
            GatewayApiService apiServiceOnly = gatewayApiServiceDao.findByDeletedFalseAndId(gatewayApiService.getId());
            if (ToolUtils.isEmpty(apiServiceOnly)) {
                throw new ObjectNotFoundException();
            } else {
                EntityUtils.resolveAllFieldsSet(gatewayApiService, apiServiceOnly);
                gatewayApiServiceDao.save(gatewayApiService);
            }
        }
    }

    @Override
    public void removeById(String id) {
        List<GatewayApiService> gatewayApiServices = gatewayApiServiceDao.findByDeletedFalseAndIdIn(ToolUtils.id2List(id));
        if (ToolUtils.notEmpty(gatewayApiServices)) {
            gatewayApiServices.forEach(gatewayApiService -> {
                gatewayApiService.setDeleted(true);
                gatewayApiService.setServiceAppEnabled(false);
            });
            gatewayApiServiceDao.save(gatewayApiServices);
            gatewayApiRouterService.removeByServiceId(id);
            Object o = gatewayApiRouterClient.refresh();
            if (ToolUtils.isEmpty(o)) {
                throw new InvokeRouteFailException();
            }
        }
    }

    @Override
    public void enableById(String id, boolean enable) {
        List<GatewayApiService> gatewayApiServices = gatewayApiServiceDao.findByDeletedFalseAndIdIn(ToolUtils.id2List(id));
        if (ToolUtils.isEmpty(gatewayApiServices)) {
            throw new ObjectNotFoundException();
        } else {
            gatewayApiServices.forEach(gatewayApiService -> {
                gatewayApiService.setServiceAppEnabled(enable);
                if (enable) { //应用服务启用路由时才获取URL地址
                    //通过应用服务AppId和应用服务Code获取得到信息
                    String appUrl = configApplication.getZookeeperData(gatewayApiService.getServiceAppId(), gatewayApiService.getServiceAppCode());
                    if (ToolUtils.isEmpty(appUrl)) {
                        throw new InvokeConfigFailException();
                    } else {
                        gatewayApiService.setServiceAppUrl(appUrl);
                    }
                }
            });
            gatewayApiServiceDao.save(gatewayApiServices);
            gatewayApiRouterService.enableByServiceId(id, enable);
            Object o = gatewayApiRouterClient.refresh();
            if (ToolUtils.isEmpty(o)) {
                throw new InvokeRouteFailException();
            }
        }
    }

    @Override
    public List<GatewayApiService> findAll() {
        return gatewayApiServiceDao.findByDeletedFalse();
    }

    @Override
    public Page<GatewayApiService> findAllByPage(SearchParam searchParam) {
        if (ToolUtils.isEmpty(searchParam.getKeywords())) {
            return gatewayApiServiceDao.findByDeletedFalse(searchParam.getPageable());
        }
        return gatewayApiServiceDao.findByDeletedFalseAndServiceAppNameLikeOrServiceAppRemarkLikeOrServiceAppVersionLike(searchParam.getLikeKeywords(), searchParam.getLikeKeywords(), searchParam.getLikeKeywords(), searchParam.getPageable());
    }

    @Override
    public GatewayApiService findById(String id) {
        return gatewayApiServiceDao.findByDeletedFalseAndId(id);
    }

    @Override
    public Object findAllApiApp() {
        Object o = gatewayApiAppClient.findAllApiApp();
        if (ToolUtils.isEmpty(o)) {
            throw new InvokeConfigFailException();
        } else {
            return o;
        }
    }
}
