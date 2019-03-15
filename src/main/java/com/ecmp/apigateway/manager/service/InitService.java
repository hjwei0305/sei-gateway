package com.ecmp.apigateway.manager.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ecmp.apigateway.config.ZKService;
import com.ecmp.apigateway.enums.InterfaceProtocolEnum;
import com.ecmp.apigateway.exception.message.MessageRuntimeException;
import com.ecmp.apigateway.manager.entity.GatewayApiService;
import com.ecmp.apigateway.manager.entity.GatewayApplication;
import com.ecmp.apigateway.manager.entity.GatewayInterface;
import com.ecmp.apigateway.manager.entity.swagger.ApplicationWithDoc;
import com.ecmp.apigateway.manager.entity.swagger.BaseRequest;
import com.ecmp.apigateway.manager.entity.swagger.SwaggerBase;
import com.ecmp.apigateway.utils.HttpUtils;
import com.ecmp.apigateway.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/29;ProjectName:api-gateway;
 */
@Service
@Slf4j
public class InitService {

    @Autowired
    private IGatewayInterfaceService gatewayInterfaceService;

    @Autowired
    private IGatewayApplicationService gatewayApplicationService;

    @Autowired
    private IGatewayApiServiceService gatewayApiServiceService;

    @Autowired
    private ZKService zkService;

    private static final String CONFIG_CENTER_API = "CONFIG_CENTER_API";


    private SwaggerBase initInterfaceByApiDoc(String apiDoc,String applicationCode){
        if(StringUtils.isBlank(applicationCode)){
            throw new MessageRuntimeException("应用代码applicationCode不能为空");
        }
        String url;
        if (StringUtils.contains(apiDoc, "api-docs")) {
            url = apiDoc.replaceAll("(api-docs.*)", "swagger.json");
        } else {
            url = apiDoc.replace("swagger-ui.html", "v2/api-docs");
        }
        SwaggerBase swaggerBase = HttpUtils.getFun(url, null, new TypeReference<SwaggerBase>() {});
        final AtomicInteger count = new AtomicInteger(0);
        if(swaggerBase != null){
            Map<String, Map<String, String>> pathInfo = JSON.parseObject(swaggerBase.getPaths(), new TypeReference<Map<String, Map<String, String>>>() {
            });
            if(pathInfo != null){

                List<GatewayInterface> cache = gatewayInterfaceService.findInterfaceByApplication(applicationCode);
                pathInfo.forEach((key, val) -> {
                    BaseRequest baseRequest = new BaseRequest();
                    if (val.containsKey("get")) {
                        baseRequest = JSON.parseObject(val.get("get"), BaseRequest.class);
                    }
                    if (val.containsKey("post")) {
                        baseRequest = JSON.parseObject(val.get("post"), BaseRequest.class);
                    }
                    if (val.containsKey("delete")) {
                        baseRequest = JSON.parseObject(val.get("delete"), BaseRequest.class);
                    }

                    boolean isFlag = false;
                    for(GatewayInterface item:cache){
                        if(key.equals(item.getInterfaceURI())){
                            isFlag = true;
                            break;
                        }
                    }
                    if(!isFlag) {
                        String interfaceName = StringUtils.isBlank(baseRequest.getDescription()) ?
                                baseRequest.getTags()[0] : baseRequest.getDescription();

                        GatewayInterface gatewayInterface = new GatewayInterface();
                        gatewayInterface.setInterfaceName(interfaceName);
                        gatewayInterface.setApplicationCode(applicationCode);
                        gatewayInterface.setDeleted(false);
                        gatewayInterface.setInterfaceProtocol(InterfaceProtocolEnum.HTTP);
                        gatewayInterface.setInterfaceURI(key);
                        gatewayInterface.setInterfaceRemark(baseRequest.getSummary());
                        gatewayInterface.setValid(true);

                        gatewayInterfaceService.addGatewayInterface(gatewayInterface);
                        count.addAndGet(1);
                    }
                });
                log.info("insert size is {}",count.get());
            }
        }
        return swaggerBase;
    }

    @Transactional
    public void initServiceByAppId(String appId){
        try{
            GatewayApiService gatewayApiService = gatewayApiServiceService.findByAppId(appId);

            Map<String, String> params = new HashMap<>();
            params.put("appId", appId);

            String configCenterUrl=zkService.getZookeeperData(appId,CONFIG_CENTER_API,CONFIG_CENTER_API);

            ApplicationWithDoc reuslt = HttpUtils.getFun(configCenterUrl+"/applicationService/findByAppId",
                    params, new TypeReference<ApplicationWithDoc>() {
                    });

            if(gatewayApiService == null) {

                GatewayApplication gatewayApplication = new GatewayApplication();
                gatewayApplication.setApplicationName(reuslt.getRemark());
                gatewayApplication.setApplicationRemark(reuslt.getRemark());

                //设置网关code
                gatewayApplication.setApplicationCode(RandomUtil.getUniqueCode());

                SwaggerBase swaggerBase = initInterfaceByApiDoc(reuslt.getApiDocsUrl(), gatewayApplication.getApplicationCode());

                gatewayApplicationService.addGatewayApplication(gatewayApplication);

                if(swaggerBase == null){
                    throw new RuntimeException("配置中心获取都文档地址不可用，请检查");
                }
                gatewayApiService = new GatewayApiService();
                gatewayApiService.setId(UUID.randomUUID().toString());
                gatewayApiService.setApplicationCode(gatewayApplication.getApplicationCode());
                gatewayApiService.setRetryAble(false);
                gatewayApiService.setServiceAppCode(reuslt.getApplicationModule().getCode());
                gatewayApiService.setServiceAppId(reuslt.getAppId());
                gatewayApiService.setServiceAppEnabled(true);
                gatewayApiService.setServiceAppUrl(swaggerBase.getBasePath());
                gatewayApiService.setServicePath(swaggerBase.getBasePath() + "/**");
                gatewayApiService.setServiceAppRemark(reuslt.getRemark());
                gatewayApiService.setServiceAppName(reuslt.getApplicationModule().getName());
                gatewayApiService.setServiceAppVersion("1.0.0");
                gatewayApiService.setStripPrefix(true);
                gatewayApiService.setDeleted(false);

                gatewayApiServiceService.save(gatewayApiService);
            }else {
                initInterfaceByApiDoc(reuslt.getApiDocsUrl(), gatewayApiService.getApplicationCode());
            }
        }catch (Exception ex){
            throw new RuntimeException("根据AppId初始化服务异常", ex);
        }
    }
}
