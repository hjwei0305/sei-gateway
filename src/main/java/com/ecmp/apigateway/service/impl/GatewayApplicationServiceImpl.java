package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.dao.GatewayApplicationDao;
import com.ecmp.apigateway.exception.ObjectNotFoundException;
import com.ecmp.apigateway.exception.RequestParamNullException;
import com.ecmp.apigateway.model.GatewayApplication;
import com.ecmp.apigateway.model.SearchParam;
import com.ecmp.apigateway.service.IGatewayApplicationService;
import com.ecmp.apigateway.service.IGatewayInterfaceService;
import com.ecmp.apigateway.utils.EntityUtils;
import com.ecmp.apigateway.utils.RandomUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author:wangdayin
 * @date:2018/4/24
 * @remark:网关应用业务接口实现
 */
@Service
@Transactional
public class GatewayApplicationServiceImpl implements IGatewayApplicationService {
    @Autowired
    private GatewayApplicationDao gatewayApplicationDao;
    @Autowired
    private IGatewayInterfaceService gatewayInterfaceService;

    @Override
    public void addGatewayApplication(GatewayApplication gatewayApplication) {
        //设置网关code
        gatewayApplication.setApplicationCode(RandomUtil.getUniqueCode());
        this.gatewayApplicationDao.save(gatewayApplication);
    }

    @Override
    public void modifyGatewayApplication(GatewayApplication gatewayApplication) {
        if (StringUtils.isEmpty(gatewayApplication.getId()) && StringUtils.isEmpty(gatewayApplication.getApplicationCode())) {
            throw new RequestParamNullException();
        }
        GatewayApplication application = this.gatewayApplicationDao.findByIdOrAndApplicationCode(gatewayApplication.getId(), gatewayApplication.getApplicationCode());
        if (null == application) throw new ObjectNotFoundException();
        EntityUtils.resolveAllFieldsSet(gatewayApplication, application);
        this.gatewayApplicationDao.save(gatewayApplication);
    }

    @Override
    public void removeGatewayApplication(String id, String applicationCode) {
        GatewayApplication application = this.gatewayApplicationDao.findByIdOrAndApplicationCode(id, applicationCode);
        if (null == application) throw new ObjectNotFoundException();
        application.setDeleted(true);
        this.gatewayApplicationDao.save(application);
        //移除应用对应的接口信息
        gatewayInterfaceService.removeGatewayInterfaceByApplicationCode(application.getApplicationCode());
    }

    @Override
    public List<GatewayApplication> findAll() {
        return this.gatewayApplicationDao.findByDeletedFalse();
    }

    @Override
    public Page<GatewayApplication> findAllByKeywordAndPage(SearchParam searchParam) {
        if (StringUtils.isEmpty(searchParam.getKeywords())) {
            return this.gatewayApplicationDao.findByDeletedFalse(searchParam.getPageable());
        }
        return this.gatewayApplicationDao.findByDeletedFalseAndApplicationNameLike(searchParam.getKeywords(), searchParam.getPageable());
    }

    @Override
    public GatewayApplication findGatewayApplicationByName(String applicationName) {
        return this.gatewayApplicationDao.findFirstByApplicationNameAndDeletedFalse(applicationName);
    }

    @Override
    public GatewayApplication findGatewayApplicationByIdOrCode(String id, String applicationCode) {
        return this.gatewayApplicationDao.findByIdOrAndApplicationCode(id, applicationCode);
    }
}
