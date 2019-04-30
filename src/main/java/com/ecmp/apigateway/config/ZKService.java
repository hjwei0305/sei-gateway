package com.ecmp.apigateway.config;

import com.ecmp.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/5/29;ProjectName:api-gateway;
 */
@Service
@Slf4j
public class ZKService {


    @Autowired
    private CuratorFramework curatorFramework;

    /**
     * 根据appId和参数键获取应用服务地址
     * @param appId 应用标识
     * @param key   应用代码
     * @return 返回应用服务API地址
     */
    public String getZookeeperData(String appId, String key) {
        return getZookeeperData(appId, key, key);
    }

    /**
     * 根据appId、参数键、配置键获取应用服务地址
     * @param appId 应用标识
     * @param key   应用代码
     * @return 返回应用服务API地址
     */
    public String getZookeeperData(String appId, String key, String mapKey) {
        Map<String, String> map = getConfigMap(appId,key);
        if(map != null){
            return map.get(mapKey);
        }
        return null;
    }

    public Map<String, String> getConfigMap(String appId, String key){
        try {
            this.checkZookeeperState();
            //应用服务的配置节点
            String jsonData = new String(curatorFramework.getData().forPath("/" + appId), "UTF-8");
            if (StringUtils.isNotBlank(jsonData)) {
                Map<String, Map<String, String>> configMap = JsonUtils.fromJson(jsonData, HashMap.class);
                return configMap.get(key);
            } else {
                log.error("获取配置中心数据异常1 ");
            }
        } catch (Exception e) {
            log.error("获取配置中心数据异常2 ",e);
        }
        return null;
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

    public void close(){
        if (curatorFramework != null) {
            curatorFramework.close();
        }
        curatorFramework = null;
    }

    public CuratorFramework getClient(){
        return this.curatorFramework;
    }
}