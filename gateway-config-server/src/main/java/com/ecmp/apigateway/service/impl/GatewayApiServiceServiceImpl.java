package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.dao.GatewayApiServiceDao;
import com.ecmp.apigateway.exception.ObjectNotFoundException;
import com.ecmp.apigateway.exception.RequestParamNullException;
import com.ecmp.apigateway.model.GatewayApiService;
import com.ecmp.apigateway.model.common.SearchParam;
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
        List<GatewayApiService> gatewayApiServices = gatewayApiServiceDao.findByDeletedFalseAndIdIn(id);
        if (ToolUtils.notEmpty(gatewayApiServices)) {
            gatewayApiServices.forEach(gatewayApiService -> gatewayApiService.setDeleted(true));
            gatewayApiServices.forEach(gatewayApiService -> gatewayApiService.setServiceAppEnabled(false));
            gatewayApiServiceDao.save(gatewayApiServices);
            //删除相关路由配置信息
            gatewayApiRouterService.removeByServiceId(id);
            //路由重新刷新
            gatewayApiRouterClient.refresh();
        }
    }

    @Override
    public void startById(String id) {
        List<GatewayApiService> gatewayApiServices = gatewayApiServiceDao.findByDeletedFalseAndIdIn(id);
        if (ToolUtils.isEmpty(gatewayApiServices)) {
            throw new ObjectNotFoundException();
        } else {
            gatewayApiServices.forEach(gatewayApiService -> gatewayApiService.setServiceAppEnabled(true));
            gatewayApiServiceDao.save(gatewayApiServices);
            //启用相关路由配置信息
            gatewayApiRouterService.enableByServiceId(id);
            //路由重新刷新
            gatewayApiRouterClient.refresh();
        }
    }

    @Override
    public void stopById(String id) {
        List<GatewayApiService> gatewayApiServices = gatewayApiServiceDao.findByDeletedFalseAndIdIn(id);
        if (ToolUtils.isEmpty(gatewayApiServices)) {
            throw new ObjectNotFoundException();
        } else {
            gatewayApiServices.forEach(gatewayApiService -> gatewayApiService.setServiceAppEnabled(false));
            gatewayApiServiceDao.save(gatewayApiServices);
            //停用相关路由配置信息
            gatewayApiRouterService.removeByServiceId(id);
            //路由重新刷新
            gatewayApiRouterClient.refresh();
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
}
