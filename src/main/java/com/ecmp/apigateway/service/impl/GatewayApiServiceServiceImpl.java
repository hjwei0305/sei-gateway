package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.dao.GatewayApiServiceDao;
import com.ecmp.apigateway.exception.ObjectNotFoundException;
import com.ecmp.apigateway.exception.RequestParamNullException;
import com.ecmp.apigateway.model.GatewayApiService;
import com.ecmp.apigateway.model.SearchParam;
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
        //这里需要通过配置中心结果获取配置的AppId；
        gatewayApiServiceDao.save(gatewayApiService);
    }

    @Override
    public void edit(GatewayApiService gatewayApiService) {
        if(ToolUtils.isEmpty(gatewayApiService.getId()) || ToolUtils.isEmpty(gatewayApiService.getServiceAppId())){
            throw new RequestParamNullException();
        } else {
            GatewayApiService apiService = gatewayApiServiceDao.findByIdOrServiceAppId(gatewayApiService.getId(), gatewayApiService.getServiceAppId());
            if(ToolUtils.isEmpty(apiService)){
                throw new ObjectNotFoundException();
            } else {
                EntityUtils.resolveAllFieldsSet(gatewayApiService, apiService);
                gatewayApiServiceDao.save(gatewayApiService);
            }
        }
    }

    @Override
    public void removeAll() {
        List<GatewayApiService> gatewayApiServiceList = gatewayApiServiceDao.findByDeletedFalse();
        if(ToolUtils.isEmpty(gatewayApiServiceList)){
            throw new ObjectNotFoundException();
        } else {
            for(GatewayApiService gatewayApiService: gatewayApiServiceList){
                if(ToolUtils.isEmpty(gatewayApiService)){
                    //这里为空时可以不任何的处理
                    //throw new ObjectNotFoundException();
                } else {
                    gatewayApiService.setDeleted(true);
                    gatewayApiServiceDao.save(gatewayApiService);
                }
            }
        }
    }

    @Override
    public void removeById(String id, String serviceAppId) {
        GatewayApiService gatewayApiService = gatewayApiServiceDao.findByIdOrServiceAppId(id, serviceAppId);
        if(ToolUtils.isEmpty(gatewayApiService)){
            throw new ObjectNotFoundException();
        } else {
            gatewayApiService.setDeleted(true);
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
        return gatewayApiServiceDao.findByDeletedFalseAndServiceAppNameLikeOrServiceAppRemarkLikeOrServiceAppVersionLike(searchParam.getKeywords(),searchParam.getKeywords(),searchParam.getKeywords(),searchParam.getPageable());
        //return gatewayApiServiceDao.findAllByPage(searchParam.getKeywords(),searchParam.getKeywords(),searchParam.getKeywords(),searchParam.getPageable());
    }

    @Override
    public GatewayApiService findById(String id, String serviceAppid) {
        return gatewayApiServiceDao.findByIdOrServiceAppId(id, serviceAppid);
    }

}
