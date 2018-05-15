package com.ecmp.apigateway;

import com.ecmp.apigateway.dao.GatewayApiServiceDao;
import com.ecmp.apigateway.exception.message.MessageRuntimeException;
import com.ecmp.apigateway.model.GatewayApiService;
import com.ecmp.apigateway.service.IGatewayApiRouterService;
import com.ecmp.apigateway.service.IGatewayApiServiceService;
import com.ecmp.apigateway.utils.ToolUtils;
import com.ecmp.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
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
    @Autowired
    private CuratorFramework curatorFramework;
    @Autowired
    private GatewayApiServiceDao gatewayApiServiceDao;
    @Autowired
    private IGatewayApiRouterService gatewayApiRouterService;
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
    public void afterPropertiesSet() throws Exception {
        //监听所有使用的应用服务AppId
        try {
            this.checkZookeeperState();
            List<GatewayApiService> gatewayApiServices = gatewayApiServiceService.findAll();
            if (ToolUtils.notEmpty(gatewayApiServices)) {
                gatewayApiServices.forEach(gatewayApiService -> {
                    try{
                        // Node watcher 监听nodeChanged事件
                        NodeCache watcher = new NodeCache(curatorFramework, gatewayApiService.getServiceAppId());
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
                                        //LogUtil.console("未获取到配置中心数据", true);
                                    }
                                } else {
                                    //LogUtil.console("未获取到配置中心数据", true);
                                }
                            }
                        });
                        watcher.start();
                    } catch (Exception e) {
                        //LogUtil.console("获取配置中心数据异常" + e.getMessage(), true);
                    }
                });
                //更新路由配置并刷新
                gatewayApiServiceDao.save(gatewayApiServices);
                gatewayApiRouterService.refresh();
            }
        } catch (Exception e) {
            //LogUtil.console("获取配置中心数据异常" + e.getMessage(), true);
        }
    }

    /**
     * Invoked by a BeanFactory on destruction of a singleton.
     * @throws Exception in case of shutdown errors. Exceptions will get logged but not rethrown to allow other beans to release their resources too.
     */
    @Override
    public void destroy() throws Exception {
        if (curatorFramework != null) {
            curatorFramework.close();
        }
        curatorFramework = null;
    }

    /**
     * 根据appId和应用代码获取应用服务地址
     * @param appId 应用标识
     * @param key   应用代码
     * @return 返回应用服务API地址
     */
    public String getZookeeperData(String appId, String key) {
        String result = null;
        try {
            this.checkZookeeperState();
            //应用服务的配置节点
            String jsonData = new String(curatorFramework.getData().forPath("/" + appId), "UTF-8");
            if (StringUtils.isNotBlank(jsonData)) {
                Map<String, Map<String, String>> configMap = JsonUtils.fromJson(jsonData, HashMap.class);
                Map<String, String> map = configMap.get(key);
                result = map.get(key);
            } else {
                //LogUtil.console("未获取到配置中心数据", true);
                throw new MessageRuntimeException("未获取到配置中心数据");
            }
        } catch (Exception e) {
            //LogUtil.console("获取配置中心数据异常" + e.getMessage(), true);
            throw new MessageRuntimeException("获取配置中心数据异常");
        }
        return result;
    }

    /**
     * 检查zk是否启动
     */
    public void checkZookeeperState() {
        // 如果zk尚未启动,则启动
        if (curatorFramework.getState() == CuratorFrameworkState.LATENT) {
            curatorFramework.start();
        }
    }
}
