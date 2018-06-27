package com.ecmp.apigateway.config;

import com.ecmp.apigateway.ZKService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.Map;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/20;ProjectName:api-gateway;
 */
@Configuration
@Slf4j
@DependsOn("client")
public class RedisConfig {

    @Autowired
    private ZKService zkService;

    @Value("${ecmp.app.id}")
    private String appId;

    @Bean
    public RedisTemplate<String, String> redisTemplate(LettuceConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 初始化 系统会维护不要调用
     */
    @Bean
    public LettuceConnectionFactory factory(){

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        //获取配置中心redis配置
        Map<String,String> configMap = zkService.getConfigMap(appId,"ECMP_BIZ_CACHE");
        log.info("ecmp_biz_cache config is {}",configMap);
        if(StringUtils.isNotBlank(configMap.get("host"))) {
            redisStandaloneConfiguration.setHostName(configMap.get("host"));
            redisStandaloneConfiguration.setPort(Integer.valueOf(configMap.get("port")));
            redisStandaloneConfiguration.setPassword(RedisPassword.of(configMap.get("password")));
            redisStandaloneConfiguration.setDatabase(Integer.valueOf(configMap.get("db")));
            LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
            lettuceConnectionFactory.setDatabase(0);
            lettuceConnectionFactory.setShareNativeConnection(true);
            return lettuceConnectionFactory;
        }
        return null;
    }
}
