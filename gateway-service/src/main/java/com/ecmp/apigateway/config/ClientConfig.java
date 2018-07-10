package com.ecmp.apigateway.config;

import com.ecmp.apigateway.ZKService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.elasticsearch.client.TransportClientFactoryBean;

import java.util.Map;


/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/8;ProjectName:monitor-center;
 */
@Configuration
@Slf4j
@Order(12)
public class ClientConfig {

    @Autowired
    private ZKService zkService;

    @Value("${ecmp.app.id}")
    private String appId;

    @Bean
    public TransportClient client() throws Exception {

        TransportClientFactoryBean factoryBean = new TransportClientFactoryBean();

        //优先从系统环境变量中读取
        Map<String, String> config = zkService.getConfigMap(appId,"ELASTICSEARCH");
        log.info("elasticsearch config is {}",config);

        factoryBean.setClusterNodes(config.get("node"));
        factoryBean.setClusterName(config.get("clusterName"));
        factoryBean.setClientTransportSniff(Boolean.FALSE);
        factoryBean.setClientIgnoreClusterName(Boolean.TRUE);
        factoryBean.setClientPingTimeout("10s");
        factoryBean.setClientNodesSamplerInterval("10s");
        factoryBean.afterPropertiesSet();
        return factoryBean.getObject();
    }
}