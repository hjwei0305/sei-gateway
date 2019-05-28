package com.ecmp.apigateway.zuul.service;

import com.ecmp.apigateway.manager.dao.GatewayInterfaceDao;
import com.ecmp.apigateway.manager.entity.GatewayApiService;
import com.ecmp.apigateway.manager.entity.GatewayInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;


/**
 * usage:接口服务
 * <p>
 * </p>
 * User:liusonglin; Date:2018/5/18;ProjectName:api-gateway;
 */
@Component
@Slf4j
public class InterfaceService {

    @Autowired
    private GatewayInterfaceDao gatewayInterfaceDao;
    @Autowired
    private RedisTemplate redisTemplate;


    public Boolean checkToken(String uri) {
//        String path = uri.substring(0, uri.indexOf("/", 2));
        return Objects.isNull(redisTemplate.opsForValue().get(key(uri)));
    }

    /**
     * 加载不需要做认证检查的接口到redis中
     */
    public void loadRuntimeData(String appCode) {
        List<GatewayInterface> interfaceList;
        if (StringUtils.isNotBlank(appCode)) {
            interfaceList = gatewayInterfaceDao.findByDeletedFalseAndIsValidTrueAndApplicationCode(appCode);
        } else {
            interfaceList = gatewayInterfaceDao.findByDeletedFalseAndIsValidTrue();
        }

        if (CollectionUtils.isEmpty(interfaceList)) {
            log.warn("未加载到接口数据");
            return;
        }

        interfaceList.forEach(gi -> {
            GatewayApiService apiService = gi.getGatewayApiService();
            if (Objects.nonNull(apiService)) {
                String path = apiService.getServicePath();
                String uri = gi.getInterfaceURI();
                if (StringUtils.isNotBlank(path) && StringUtils.isNotBlank(uri)) {
                    path = path.replaceAll("[/|*]", "");
                    if (!gi.getValidateToken()) {
                        redisTemplate.opsForValue().set(key("/" + path + uri), 1);
                    }
                }
            }
        });
    }

    private String key(String uri) {
        return "Gateway:NoToken" + uri.replaceAll("/", ":");
    }
}
