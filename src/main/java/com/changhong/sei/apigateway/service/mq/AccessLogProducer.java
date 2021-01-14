package com.changhong.sei.apigateway.service.mq;

import com.changhong.sei.apigateway.commons.Constants;
import com.changhong.sei.apigateway.entity.vo.AccessLogVo;
import com.changhong.sei.core.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 实现功能：访问日志记录队列生产者
 * 访问日志在网关产生并推送到kafka中,在auth进行消费
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-01-13 09:49
 */
@Component
@ConditionalOnBean({ProducerFactory.class, KafkaTemplate.class})
public class AccessLogProducer {
    private static final Logger LOG = LoggerFactory.getLogger(AccessLogProducer.class);
    @Autowired(required = false)
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 发送消息
     *
     * @param accessLog 消息
     */
    public void send(AccessLogVo accessLog) {
        if (Objects.isNull(accessLog) || Objects.isNull(kafkaTemplate)) {
            return;
        }
        String message = JsonUtils.toJson(accessLog);

        kafkaTemplate.send(Constants.TOPIC_ACCESS_LOG, accessLog.getTraceId(), message);
        if (LOG.isDebugEnabled()) {
            LOG.debug("访问记录队列生产者：" + message);
        }
    }
}
