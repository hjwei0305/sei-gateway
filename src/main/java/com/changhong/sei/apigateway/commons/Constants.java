package com.changhong.sei.apigateway.commons;

import org.springframework.boot.actuate.autoconfigure.cloudfoundry.AccessLevel;

/**
 * 实现功能：常量定义
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2021-01-13 09:52
 */
public interface Constants {

    /**
     * 访问开始时间戳
     */
    String REQUEST_ATTRIBUTE_START_TIME = "SeiStartTime";
    /**
     * token
     */
    String REQUEST_ATTRIBUTE_TOKEN = "SeiAuthToken";

    /**
     * 访问日志topic
     */
    String TOPIC_ACCESS_LOG = "SeiGatewayAccessLog";

    /**
     * swagger2默认的url后缀
     */
    String SWAGGER2URL = "/v2/api-docs";
}
