package com.ecmp.apigateway.config;

import com.ecmp.config.util.ZkClient;
import com.ecmp.context.common.ConfigConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.WebFilter;

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
    @Value("${ECMP_CONFIG_CENTER}")
    private String connects;

    /**
     * 构建一个zk客户端<p>
     * connects zk连接地址 集群以逗号分隔，如10.4.68.45:2181,10.4.68.46:2182,10.4.68.47:2183
     * @return zk客户端
     */
    @Bean
    public CuratorFramework curatorFramework() {
        //实例化zk客户端
        ZkClient zkClient = new ZkClient(connects, ConfigConstants.ZK_NAME_SPACE);
        //初始化zk
        zkClient.init();
        return zkClient.getClient();
    }

    @Bean
    public WebFilter contextPathWebFilter() {
        String contextPath = "/api-gateway";
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if (request.getURI().getPath().startsWith(contextPath)) {
                return chain.filter(
                        exchange.mutate()
                                .request(request.mutate().contextPath(contextPath).build())
                                .build());
            }
            return chain.filter(exchange);
        };
    }
}
