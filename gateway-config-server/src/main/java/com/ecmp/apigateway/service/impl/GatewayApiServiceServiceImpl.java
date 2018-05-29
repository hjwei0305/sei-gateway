package com.ecmp.apigateway.service.impl;

import com.ecmp.apigateway.ConfigCenterContextApplication;
import com.ecmp.apigateway.ZKService;
import com.ecmp.apigateway.dao.GatewayApiServiceDao;
import com.ecmp.apigateway.exception.InvokeConfigFailException;
import com.ecmp.apigateway.exception.ObjectNotFoundException;
import com.ecmp.apigateway.exception.RequestParamNullException;
import com.ecmp.apigateway.model.GatewayApiService;
import com.ecmp.apigateway.model.common.SearchParam;
import com.ecmp.apigateway.service.IGatewayApiServiceService;
import com.ecmp.apigateway.utils.EntityUtils;
import com.ecmp.apigateway.utils.ToolUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/25
 * @remark: 应用服务-业务层实现
 * @update：liusonglin 去掉routerService,所有router信息放到service里，事物未提交前调用刷新不会跟新路由
 */
@Service
@Transactional
public class GatewayApiServiceServiceImpl implements IGatewayApiServiceService {

    private static final Logger logger = LoggerFactory.getLogger(GatewayApiServiceServiceImpl.class);

    @Autowired
    private GatewayApiServiceDao gatewayApiServiceDao;

    @Autowired
    private ZKService zkService;

    @Value("${gateway.route.service.url}")
    private String gateWayUrl;

    @Value("${gateway.route.service.path}")
    private String gateWayPath;

    @Override
    public void save(GatewayApiService gatewayApiService) {
        if (ToolUtils.isEmpty(gatewayApiService.getServiceAppId()) || ToolUtils.isEmpty(gatewayApiService.getApplicationCode())) {
            throw new RequestParamNullException();
        } else {
            String appUrl = zkService.getZookeeperData(gatewayApiService.getServiceAppId(),
                    gatewayApiService.getServiceAppCode());
            gatewayApiService.setServiceAppUrl(appUrl);
            gatewayApiService.setServicePath(ToolUtils.key2Path(ToolUtils.getRouteKey(appUrl)));
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
                if (enable) { //*应用服务启用路由时才获取
                    //*通过应用服务AppId和应用服务Code获取
                    String appUrl = zkService.getZookeeperData(gatewayApiService.getServiceAppId(), gatewayApiService.getServiceAppCode());
                    if (ToolUtils.isEmpty(appUrl)) {
                        throw new InvokeConfigFailException();
                    } else {
                        gatewayApiService.setServiceAppUrl(appUrl);
                    }
                }
            });
            gatewayApiServiceDao.save(gatewayApiServices);
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
        //todo 查询所有路由
        return null;
    }

    @Override
    public Object refresh() {
        //优先从系统环境变量中读取
        String gatewayHost;
        String appId = System.getenv("ECMP_APP_ID");
        if (StringUtils.isNotBlank(appId)) {
            String apiGatewayHost =zkService.getZookeeperData(appId,"API_GATEWAY_HOST");
            gatewayHost = apiGatewayHost;
        } else {
            gatewayHost=gateWayUrl;
        }


        HttpGet get =new HttpGet(gatewayHost+gateWayPath);
        try(CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response =httpClient.execute(get)){
            return org.apache.http.util.EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            logger.error("refresh error",e);
        }
        return null;
    }
}
