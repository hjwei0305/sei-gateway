package com.changhong.sei.apigateway.service;

import com.changhong.sei.apigateway.service.client.AuthWhitelistClient;
import com.changhong.sei.apigateway.service.client.dto.AuthWhitelistDto;
import com.changhong.sei.core.dto.ResultData;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * usage:接口服务
 * <p>
 * </p>
 * User:liusonglin; Date:2018/5/18;ProjectName:api-gateway;
 */
@Component
public class InterfaceService {
    private static final Logger log = LoggerFactory.getLogger(InterfaceService.class);

    private static final Lock LOCK = new ReentrantLock();

    @Value("${sei.application.env}")
    private String envCode;
    @Autowired
    private AuthWhitelistClient whitelistClient;

    /**
     * 缓存
     */
    private static Cache<String, Object> cacheContainer = buildCacheContainer();

    private static Cache<String, Object> buildCacheContainer() {
        return CacheBuilder.newBuilder()
                // 设置缓存最大容量为300，超过300之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(300)
                // 设置写缓存后8秒钟过期  最后一次写入后的一段时间移出
//                .expireAfterWrite(600000, TimeUnit.MILLISECONDS)
                //最后一次访问后的一段时间移出
                .expireAfterAccess(600000, TimeUnit.MILLISECONDS)

                // 设置并发级别为8，并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(8)
                // 设置缓存容器的初始容量为10
                .initialCapacity(10)
                // 开启统计缓存的命中率功能
                .recordStats()
                .build();
    }

    /**
     * 忽略token认证的url
     */
    private final Set<String> ignoreAuthURLSet = new HashSet<>();

    public Boolean checkToken(String uri) {
        Object val = cacheContainer.getIfPresent(uri);
        if (Objects.isNull(val)) {
            String[] arr = new String[ignoreAuthURLSet.size()];
            //Set-->数组
            ignoreAuthURLSet.toArray(arr);
            if (StringUtils.containsAny(uri, arr)) {
                cacheContainer.put(uri, "1");
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 加载不需要做认证检查的接口到redis中
     */
    public void loadRuntimeData() {
        ignoreAuthURLSet.clear();
        ResultData<List<AuthWhitelistDto>> resultData = whitelistClient.get(envCode);
        if (resultData.failed()) {
            log.error(resultData.getMessage());
            return;
        }
        List<AuthWhitelistDto> whitelists = resultData.getData();
        if (CollectionUtils.isEmpty(whitelists)) {
            log.warn("未加载到接口数据");
        } else {
            whitelists.forEach(gi -> {
                ignoreAuthURLSet.add(gi.getUri());
            });
        }
    }

    public Boolean reloadCache() {
        LOCK.lock();
        try {
            this.loadRuntimeData();

            cacheContainer.invalidateAll();
        } finally {
            LOCK.unlock();
        }
        return true;
    }
}
