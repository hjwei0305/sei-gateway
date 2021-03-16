package com.changhong.sei.apigateway.config;

import com.changhong.sei.apigateway.commons.redismq.SubscribeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * 实现功能：
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-03-16 08:19
 */
@Configuration
public class RedisMqConfig {
    private final RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public RedisMqConfig(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    /**
     * 配置消息监听器
     */
    @Bean
    public SubscribeListener listener() {
        return new SubscribeListener();
    }

    /**
     * 配置 发布/订阅 的 Topic
     */
    @Bean
    public ChannelTopic gatewayChannelTopic() {
        return new ChannelTopic("gateway:config");
    }

    /**
     * <h2>将消息监听器绑定到消息容器</h2>
     */
    @Bean
    public RedisMessageListenerContainer messageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);

        //  MessageListener 监听数据
        container.addMessageListener(listener(), gatewayChannelTopic());
        return container;
    }
}
