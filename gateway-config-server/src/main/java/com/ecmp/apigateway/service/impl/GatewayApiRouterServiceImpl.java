package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.dao.GatewayApiRouterDao;
import com.ecmp.apigateway.exception.ObjectNotFoundException;
import com.ecmp.apigateway.exception.RequestParamNullException;
import com.ecmp.apigateway.model.GatewayApiRouter;
import com.ecmp.apigateway.model.SearchParam;
import com.ecmp.apigateway.service.IGatewayApiRouterService;
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
 * @remark: 路由配置-业务层实现
 */
@Service
@Transactional
public class GatewayApiRouterServiceImpl implements IGatewayApiRouterService {
    @Autowired
    private GatewayApiRouterDao gatewayApiRouterDao;

    @Override
    public void save(GatewayApiRouter gatewayApiRouter) {
        gatewayApiRouterDao.save(gatewayApiRouter);
    }

    @Override
    public void edit(GatewayApiRouter gatewayApiRouter) {
        if (ToolUtils.isEmpty(gatewayApiRouter.getId())) {
            throw new RequestParamNullException();
        } else {
            GatewayApiRouter apiRouter = gatewayApiRouterDao.findById(gatewayApiRouter.getId());
            if (ToolUtils.isEmpty(apiRouter)) {
                throw new ObjectNotFoundException();
            } else {
                EntityUtils.resolveAllFieldsSet(gatewayApiRouter, apiRouter);
                gatewayApiRouterDao.save(gatewayApiRouter);
            }
        }
    }

    @Override
    public void removeAll() {
        List<GatewayApiRouter> gatewayApiRouterList = gatewayApiRouterDao.findByDeletedFalse();
        if (ToolUtils.isEmpty(gatewayApiRouterList)) {
            throw new ObjectNotFoundException();
        } else {
            for (GatewayApiRouter gatewayApiRouter : gatewayApiRouterList) {
                if (ToolUtils.isEmpty(gatewayApiRouter)) {
                    //这里为空时可以不做任何的处理
                    //throw new ObjectNotFoundException();
                } else {
                    gatewayApiRouter.setDeleted(true);
                    gatewayApiRouter.setEnabled(false);
                    gatewayApiRouterDao.save(gatewayApiRouter);
                }
            }
        }
    }

    @Override
    public void removeById(String id) {
        GatewayApiRouter gatewayApiRouter = gatewayApiRouterDao.findById(id);
        if (ToolUtils.isEmpty(gatewayApiRouter)) {
            throw new ObjectNotFoundException();
        } else {
            gatewayApiRouter.setDeleted(true);
            gatewayApiRouter.setEnabled(false);
            gatewayApiRouterDao.save(gatewayApiRouter);
        }
    }

    @Override
    public List<GatewayApiRouter> findAll() {
        return gatewayApiRouterDao.findByDeletedFalse();
    }

    @Override
    public Page<GatewayApiRouter> findAllByPage(SearchParam searchParam) {
        if (ToolUtils.isEmpty(searchParam.getKeywords())) {
            return gatewayApiRouterDao.findByDeletedFalse(searchParam.getPageable());
        }
        return gatewayApiRouterDao.findByDeletedFalseAndPathLikeOrServiceIdLikeOrInterfaceNameLike(searchParam.getLikeKeywords(), searchParam.getLikeKeywords(), searchParam.getLikeKeywords(), searchParam.getPageable());
    }

    @Override
    public GatewayApiRouter findById(String id) {
        return gatewayApiRouterDao.findById(id);
    }

}
