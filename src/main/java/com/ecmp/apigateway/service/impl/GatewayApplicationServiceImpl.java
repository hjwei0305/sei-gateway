package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.dao.GatewayApplicationDao;
import com.ecmp.apigateway.enums.OperationTypeEnum;
import com.ecmp.apigateway.exception.ObjectNotFoundException;
import com.ecmp.apigateway.exception.RequestParamNullException;
import com.ecmp.apigateway.entity.GatewayApplication;
import com.ecmp.apigateway.entity.common.SearchParam;
import com.ecmp.apigateway.service.IGatewayApplicationService;
import com.ecmp.apigateway.service.IGatewayInterfaceService;
import com.ecmp.apigateway.utils.EntityUtils;
import com.ecmp.apigateway.utils.ToolUtils;
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
        this.gatewayApplicationDao.save(gatewayApplication);
    }

    @Override
    public void modifyGatewayApplication(GatewayApplication gatewayApplication) {
        if (ToolUtils.isEmpty(gatewayApplication.getId()) && ToolUtils.isEmpty(gatewayApplication.getApplicationCode())) {
            throw new RequestParamNullException();
        }
        GatewayApplication application = this.gatewayApplicationDao.findByIdOrApplicationCode(gatewayApplication.getId(), gatewayApplication.getApplicationCode());
        if (null == application) throw new ObjectNotFoundException();
        EntityUtils.resolveAllFieldsSet(gatewayApplication, application);
        this.gatewayApplicationDao.save(gatewayApplication);
    }

    @Override
    public void removeGatewayApplication(String id, String applicationCode) {
        GatewayApplication application = this.gatewayApplicationDao.findByIdOrApplicationCode(id, applicationCode);
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
        if (ToolUtils.isEmpty(searchParam.getKeywords())) {
            return this.gatewayApplicationDao.findByDeletedFalse(searchParam.getPageable());
        }
        return this.gatewayApplicationDao.findByDeletedFalseAndApplicationNameLike(searchParam.getLikeKeywords(), searchParam.getPageable());
    }

    @Override
    public List<GatewayApplication> findGatewayApplicationByName(String applicationName) {
        return this.gatewayApplicationDao.findByApplicationNameAndDeletedFalse(applicationName);
    }

    @Override
    public GatewayApplication findGatewayApplicationByIdOrCode(String id, String applicationCode) {
        return this.gatewayApplicationDao.findByIdOrApplicationCode(id, applicationCode);
    }

    @Override
    public boolean checkApplicationName(String applicationName, OperationTypeEnum operationType) {
        List<GatewayApplication> applications = this.gatewayApplicationDao.findByApplicationNameAndDeletedFalse(applicationName);
        return OperationTypeEnum.checkOperationType(operationType, applications);
    }
}
