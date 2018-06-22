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
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/20;ProjectName:api-gateway;
 */
@Configuration
@Slf4j
public class RedisConfig {

    @Autowired
    private ZKService zkService;

    @Value("${ecmp.app.id}")
    private String appId;

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
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
    public RedisConnectionFactory factory(){
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(32);
        poolConfig.setMaxIdle(16);
        poolConfig.setMaxWaitMillis(10000);
        poolConfig.setTestOnBorrow(false);
        //获取配置中心redis配置
        Map<String,String> configMap = zkService.getConfigMap(appId,"ECMP_BIZ_CACHE");
        log.info("ecmp_biz_cache config is {}",configMap);

        jedisConnectionFactory.setPoolConfig(poolConfig);
        jedisConnectionFactory.setUsePool(true);

        if(StringUtils.isNotBlank(configMap.get("host"))) {
            jedisConnectionFactory.setHostName(configMap.get("host"));
            jedisConnectionFactory.setPort(Integer.valueOf(configMap.get("port")));
            jedisConnectionFactory.setPassword(configMap.get("password"));
            jedisConnectionFactory.setDatabase(Integer.valueOf(configMap.get("db")));
            return jedisConnectionFactory;
        }
        return null;
    }
}
