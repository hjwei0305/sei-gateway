package com.ecmp.apigateway.utils;

import com.ecmp.apigateway.enums.RedisEnum;
import com.ecmp.apigateway.model.vo.SessionUser;
import com.ecmp.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/20;ProjectName:api-gateway;
 */
@Component
@Slf4j
public class RedisUtils {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 存储缓存
     *
     * @param key
     * @param t
     * @param expired
     * @param <T>
     * @return
     */
    public  <T> boolean  saveCache(String key, T t, long expired){
        try {
            redisTemplate.opsForValue().set(RedisEnum.ROUTER_SERVICE_PREFIX.getKey()+
                    key, JsonUtils.toJson(t), expired, TimeUnit.MILLISECONDS);
            return true;
        } catch (Exception ex) {
            log.error("存储缓存出错",ex);
        }
        return false;
    }

    /**
     * 获取缓存
     *
     * @param key
     * @param <T>
     * @return
     */
    public  <T> T getCache(String key,Class<T> tClass){
        try {
            String result = redisTemplate.opsForValue().get(RedisEnum.ROUTER_SERVICE_PREFIX.getKey()+key);
            return JsonUtils.fromJson(result,tClass);
        }catch (Exception ex){
            log.error("获取缓存出错",ex);
        }
        return null;
    }

    /**
     * 用户计数器,因为redis不支持单个key的过期时间，所以用排序队列，
     * 以当前时间为排序值
     *
     * @param sessionUser
     * @return
     */
    public void counterAdd(SessionUser sessionUser){
        try {
            redisTemplate.opsForZSet().add(RedisEnum.ONLINE_USER_COUNTER.getKey()
                    ,sessionUser.getUserId(),System.currentTimeMillis());
        }catch (Exception ex){
            log.error("计数器出错：",ex);
        }

    }

    /**
     * 每次请求当前人数都是取去掉排序值为5分钟前的人数
     *
     * @return
     */
    public long getCurrentCount(){
        redisTemplate.opsForZSet().removeRangeByScore(RedisEnum.ONLINE_USER_COUNTER.getKey(),
                0L,System.currentTimeMillis()-5*60*1000);

        long currentCount = redisTemplate.opsForZSet().size(RedisEnum.ONLINE_USER_COUNTER.getKey());
        log.info("currentCount is {}",currentCount);
        return currentCount;
    }


}