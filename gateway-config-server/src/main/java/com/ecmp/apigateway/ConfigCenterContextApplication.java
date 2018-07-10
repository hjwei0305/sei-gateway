package com.ecmp.apigateway;

import com.ecmp.apigateway.dao.GatewayApiServiceDao;
import com.ecmp.apigateway.model.GatewayApiService;
import com.ecmp.apigateway.service.IGatewayApiServiceService;
import com.ecmp.apigateway.service.impl.GatewayApiServiceServiceImpl;
import com.ecmp.apigateway.utils.ToolUtils;
import com.ecmp.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/5/4 0:03
 * @update hejun by 2018/5/4
 */
@Component
public class ConfigCenterContextApplication implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(GatewayApiServiceServiceImpl.class);

    @Autowired
    private ZKService zkService;
    @Autowired
    private GatewayApiServiceDao gatewayApiServiceDao;
    @Autowired
    private IGatewayApiServiceService gatewayApiServiceService;

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     * @throws Exception in the event of misconfiguration (such as failure to set an essential property) or if initialization fails.
     */
    @Override
    public void afterPropertiesSet() {
        //监听所有使用的应用服务AppId
        try {
            zkService.checkZookeeperState();
            List<GatewayApiService> gatewayApiServices = gatewayApiServiceService.findAll();
            if (ToolUtils.notEmpty(gatewayApiServices)) {
                gatewayApiServices.forEach(gatewayApiService -> {
                    try{
                        // Node watcher 监听nodeChanged事件
                        NodeCache watcher = new NodeCache(zkService.getClient(), "/"+gatewayApiService.getServiceAppId());
                        watcher.getListenable().addListener(() -> {
                            ChildData nodeData = watcher.getCurrentData();
                            //监听发现到内容有变化时
                            if (Objects.nonNull(nodeData)) {
                                //初始化或更新应用服务的全局参数
                                String jsonData = new String(nodeData.getData(), "UTF-8");
                                if (StringUtils.isNotBlank(jsonData)) {
                                    Map<String, Map<String, String>> configMap = JsonUtils.fromJson(jsonData, HashMap.class);
                                    if (Objects.nonNull(configMap) && !configMap.isEmpty()) {
                                        //触发更新路由配置相关信息
                                        Map<String, String> map = configMap.get(gatewayApiService.getServiceAppCode());
                                        String newAppUrl = map.get(gatewayApiService.getServiceAppCode());
                                        gatewayApiService.setServiceAppUrl(newAppUrl);
                                    } else {
                                        logger.error("未获取到配置中心数据");
                                    }
                                } else {
                                    logger.error("未获取到配置中心数据");
                                }
                            }
                        });
                        watcher.start();
                    } catch (Exception e) {
                        logger.error("未获取到配置中心数据", e);
                    }
                    gatewayApiServiceDao.save(gatewayApiService);
                });
                //更新路由配置并刷新
                gatewayApiServiceService.refresh();
            }
        } catch (Exception e) {
            logger.error("未获取到配置中心数据");
        }
    }

    /**
     * Invoked by a BeanFactory on destruction of a singleton.
     * @throws Exception in case of shutdown errors. Exceptions will get logged but not rethrown to allow other beans to release their resources too.
     */
    @Override
    public void destroy() throws Exception {
        zkService.close();
    }
}