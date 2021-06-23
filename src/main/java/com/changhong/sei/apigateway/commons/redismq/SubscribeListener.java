package com.changhong.sei.apigateway.commons.redismq;

import com.changhong.sei.apigateway.service.AuthWhitelistService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * 实现功能：消息监听器
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-03-16 08:17
 */
public class SubscribeListener implements MessageListener, InitializingBean {

    @Autowired
    private AuthWhitelistService interfaceService;

    @Override
    public void afterPropertiesSet() throws Exception {
        interfaceService.reloadConfigCache();
    }

    /**
     * 消息回调
     * 发送消息: redisTemplate.convertAndSend(topic.getTopic(), "hello.");
     *
     * @param message {@link Message} 消息体 + ChannelName
     * @param pattern 订阅的 pattern, ChannelName 的模式匹配
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        interfaceService.reloadConfigCache();
//        String body = new String(message.getBody());
//        String channel = new String(message.getChannel());
//        String pattern_ = new String(pattern);
//
//        System.out.println(body);
//        System.out.println(channel);
//        // 如果是 ChannelTopic, 则 channel 字段与 pattern 字段值相同
//        System.out.println(pattern_);
    }
}
