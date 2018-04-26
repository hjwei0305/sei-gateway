package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.dao.GatewayInterfaceDao;
import com.ecmp.apigateway.exception.ObjectNotFoundException;
import com.ecmp.apigateway.exception.RequestParamNullException;
import com.ecmp.apigateway.model.GatewayInterface;
import com.ecmp.apigateway.model.SearchParam;
import com.ecmp.apigateway.service.IGatewayInterfaceService;
import com.ecmp.apigateway.utils.EntityUtils;
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
 * @remark: 网关-接口业务实现
 */
@Service
@Transactional
public class GatewayInterfaceServiceImpl implements IGatewayInterfaceService {
    @Autowired
    private GatewayInterfaceDao gatewayInterfaceDao;

    @Override
    public void addGatewayInterface(GatewayInterface gatewayInterface) {
        this.gatewayInterfaceDao.save(gatewayInterface);
    }

    @Override
    public void removeGatewayInterface(String id) {
        if (StringUtils.isNotEmpty(id)) {
            GatewayInterface gatewayInterface = this.gatewayInterfaceDao.getOne(id);
            if (null == gatewayInterface) throw new ObjectNotFoundException();
            gatewayInterface.setDeleted(true);
            this.gatewayInterfaceDao.save(gatewayInterface);
        } else {
            throw new RequestParamNullException();
        }
    }

    @Override
    public void removeGatewayInterfaceByApplicationCode(String applicationCode) {
        if (StringUtils.isNotEmpty(applicationCode)) {
            List<GatewayInterface> gatewayInterfaces = this.gatewayInterfaceDao.findByDeletedFalseAndApplicationCode(applicationCode);
            if (CollectionUtils.isNotEmpty(gatewayInterfaces)) {
                gatewayInterfaces.forEach(gatewayInterface -> gatewayInterface.setDeleted(true));
                this.gatewayInterfaceDao.save(gatewayInterfaces);
            }
        }
    }

    @Override
    public void modifyGatewayInterface(GatewayInterface gatewayInterface) {
        GatewayInterface gateway = this.gatewayInterfaceDao.findByDeletedFalseAndId(gatewayInterface.getId());
        if (null == gateway) throw new ObjectNotFoundException();
        EntityUtils.resolveAllFieldsSet(gatewayInterface, gateway);
        this.gatewayInterfaceDao.save(gatewayInterface);
    }

    @Override
    public GatewayInterface findGatewayInterfaceById(String id) {
        GatewayInterface gatewayInterface = this.gatewayInterfaceDao.findByDeletedFalseAndId(id);
        if (null == gatewayInterface) throw new ObjectNotFoundException();
        return gatewayInterface;
    }

    @Override
    public Page<GatewayInterface> findGatewayInterfaceByPage(String applicationCode, SearchParam searchParam) {
        if (StringUtils.isNotEmpty(searchParam.getKeywords())) {
            Page<GatewayInterface> gatewayInterfaces = this.gatewayInterfaceDao.findByDeletedFalseAndApplicationCodeOrInterfaceNameLikeOrInterfaceURILike(applicationCode, searchParam.getLikeKeywords(), searchParam.getLikeKeywords(), searchParam.getPageable());
            return gatewayInterfaces;
        }
        return this.gatewayInterfaceDao.findByDeletedFalseAndApplicationCode(applicationCode, searchParam.getPageable());
    }

    @Override
    public GatewayInterface findGatewayInterfaceByNameOrURI(String applicationCode, String interfaceName, String interfaceURI) {
        return this.gatewayInterfaceDao.findFirstByApplicationCodeAndInterfaceNameOrInterfaceURI(applicationCode, interfaceName, interfaceURI);
    }

    @Override
    public List<GatewayInterface> findGatewayInterfaceByNoPage(String applicationCode) {
        return this.gatewayInterfaceDao.findByDeletedFalseAndApplicationCode(applicationCode);
    }
}
