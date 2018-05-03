package com.ecmp.apigateway;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;

@SpringBootApplication
@EnableSpringConfigured
@ServletComponentScan
@EnableFeignClients
public class GatewayConfigServerApplication {

    @Value("${ecmp.config.center}")
    private String connects;
    /**
     * 配置中心zookeeper的节点命名空间
     */
    @Value("${ecmp.config.center.namespace}")
    private String namespace;

    public static void main(String[] args) {
        SpringApplication.run(GatewayConfigServerApplication.class, args);
    }

    /**
     * 构建一个zk客户端
     * <p>
     * connects zk连接地址 集群以逗号分隔，如10.4.68.45:2181,10.4.68.46:2182,10.4.68.47:2183
     *
     * @return zk客户端
     */
    @Bean
    public CuratorFramework curatorFramework() {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        CuratorFramework client = builder
                .connectString(connects)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .canBeReadOnly(true)
                .retryPolicy(new ExponentialBackoffRetry(1000, 10))
                .namespace(namespace)
                .defaultData(null)
                .build();
        client.start();
        return client;
    }

}
