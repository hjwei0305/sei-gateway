package com.changhong.sei.apigateway.service;

import com.changhong.sei.apigateway.dao.GatewayInterfaceDao;
import com.changhong.sei.apigateway.entity.GatewayInterface;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

/**
 * usage:接口服务
 * <p>
 * </p>
 * User:liusonglin; Date:2018/5/18;ProjectName:api-gateway;
 */
@Component
public class InterfaceService {
    private static final Logger log = LoggerFactory.getLogger(InterfaceService.class);

    @Autowired
    private GatewayInterfaceDao gatewayInterfaceDao;

    private static Lock lock = new ReentrantLock();

    /**
     * 缓存
     */
    private static Cache<String, Object> cacheContainer = buildCacheContainer();

    private static Cache<String, Object> buildCacheContainer() {
        return CacheBuilder.newBuilder()
                // 设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
                .maximumSize(100)
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
    private final Set<Pattern> ignoreAuthURLSet = new HashSet<>();

    public Boolean checkToken(String uri) {
        Object val = cacheContainer.getIfPresent(uri);
        if (Objects.isNull(val)) {
            for (Pattern pattern : this.ignoreAuthURLSet) {
                if (pattern.matcher(uri).matches()) {
                    cacheContainer.put(uri, "1");
                    return false;
                }
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
        List<GatewayInterface> interfaceList = gatewayInterfaceDao.findAll();
        if (CollectionUtils.isEmpty(interfaceList)) {
            log.warn("未加载到接口数据");
        } else {
            interfaceList.forEach(gi -> {
                if (!gi.getValidateToken() && !gi.isDeleted()) {
                    ignoreAuthURLSet.add(Pattern.compile(".*?" + gi.getInterfaceURI() + ".*", Pattern.CASE_INSENSITIVE));
                }
            });
        }
    }

    public List<GatewayInterface> findByAppCode(String appCode) {
        return gatewayInterfaceDao.findByDeletedFalseAndApplicationCode(appCode);
    }

    public GatewayInterface save(GatewayInterface gi) {
        gi = gatewayInterfaceDao.save(gi);

        reloadCache();
        return gi;
    }

    public void delete(String id) {
        Optional<GatewayInterface> gi = gatewayInterfaceDao.findById(id);
        if (gi.isPresent()) {
            gi.get().setDeleted(true);
            gatewayInterfaceDao.save(gi.get());

            reloadCache();
        }
    }

    public Boolean reloadCache() {
        lock.lock();
        try {
            this.loadRuntimeData();

            cacheContainer.invalidateAll();
        } finally {
            lock.unlock();
        }
        return true;
    }
}
