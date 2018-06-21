package com.ecmp.apigateway;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/5/29;ProjectName:api-gateway;
 */
@Configuration
public class ZKconfig {

    /**
     * 配置中心zookeeper的连接地址
     */
    @Value("${ecmp.config.center}")
    private String connects;
    /**
     * 配置中心zookeeper的节点命名空间
     */
    @Value("${ecmp.config.center.namespace}")
    private String namespace;

    /**
     * 构建一个zk客户端<p>
     * connects zk连接地址 集群以逗号分隔，如10.4.68.45:2181,10.4.68.46:2182,10.4.68.47:2183
     * @return zk客户端
     */
    @Bean
    public CuratorFramework curatorFramework() {
        //优先从系统环境变量中读取
        String zkHost = System.getenv().get("ECMP_CONFIG_CENTER");
        if (StringUtils.isBlank(zkHost)) {
            zkHost = connects;
        }

        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        CuratorFramework client = builder
                .connectString(zkHost)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .canBeReadOnly(true)
                .retryPolicy(new ExponentialBackoffRetry(1000, 1))
                .namespace(namespace)
                .defaultData(null)
                .build();
        client.start();
        return client;
    }
}
