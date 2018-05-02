package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.dao.GatewayApiServiceDao;
import com.ecmp.apigateway.exception.ObjectNotFoundException;
import com.ecmp.apigateway.exception.RequestParamNullException;
import com.ecmp.apigateway.model.GatewayApiService;
import com.ecmp.apigateway.model.common.SearchParam;
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
        GatewayApiService gatewayApiService = gatewayApiServiceDao.findByDeletedFalseAndId(id);
        if (ToolUtils.isEmpty(gatewayApiService)) {
            throw new ObjectNotFoundException();
        } else {
            gatewayApiService.setDeleted(true);
            gatewayApiService.setServiceAppEnabled(false);
            gatewayApiServiceDao.save(gatewayApiService);
        }
    }

    @Override
    public void enableById(String id) {
        GatewayApiService gatewayApiService = gatewayApiServiceDao.findByDeletedFalseAndId(id);
        if (ToolUtils.isEmpty(gatewayApiService)) {
            throw new ObjectNotFoundException();
        } else {
            gatewayApiService.setDeleted(false);
            gatewayApiService.setServiceAppEnabled(true);
            gatewayApiServiceDao.save(gatewayApiService);
        }
    }

    @Override
    public void disableById(String id) {
        GatewayApiService gatewayApiService = gatewayApiServiceDao.findByDeletedFalseAndId(id);
        if (ToolUtils.isEmpty(gatewayApiService)) {
            throw new ObjectNotFoundException();
        } else {
            gatewayApiService.setDeleted(false);
            gatewayApiService.setServiceAppEnabled(false);
            gatewayApiServiceDao.save(gatewayApiService);
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
