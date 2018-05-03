package com.ecmp.apigateway;

import com.ecmp.util.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 马超(Vision.Mac)
 * @version 1.0.1 2018/5/4 0:03
 */
@Component
public class ConfigCenterContext implements DisposableBean {
    @Autowired
    private CuratorFramework curatorFramework;

    /**
     * Invoked by a BeanFactory on destruction of a singleton.
     *
     * @throws Exception in case of shutdown errors.
     *                   Exceptions will get logged but not rethrown to allow
     *                   other beans to release their resources too.
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
     *
     * @param appId 应用标识
     * @param key   应用代码
     * @return 返回应用服务API地址
     */
    public String getZookeeperData(String appId, String key) {
        String result = null;
        try {
            //应用服务的配置节点
            String jsonData = new String(curatorFramework.getData().forPath("/" + appId), "UTF-8");
            if (StringUtils.isNotBlank(jsonData)) {
                Map<String, Map<String, String>> configMap = JsonUtils.fromJson(jsonData, HashMap.class);
                Map<String, String> map = configMap.get(key);
                result = map.get(key);
            } else {
//                LogUtil.console("未获取到配置中心数据", true);
            }
        } catch (Exception e) {
//            LogUtil.console("获取配置中心数据异常" + e.getMessage(), true);
        }
        return result;
    }
}