package com.ecmp.apigateway.schedul;

import com.ecmp.apigateway.dao.UserCounterDao;
import com.ecmp.apigateway.model.UserCounter;
import com.ecmp.apigateway.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/27;ProjectName:api-gateway;
 */
@Component
@Slf4j
public class UserCounterTask {

    @Autowired
    private UserCounterDao userCounterDao;

    @Autowired
    private RedisUtils redisUtils;

    @Scheduled(fixedDelay=1000*60*5)
    public void addCounterToES(){
        Instant instant = Instant.now();
        log.info("执行定时任务，写入当前在线用户到es，当前时间：{}", instant);
        List<String> result = redisUtils.getCurrentUser();
        UserCounter userCounter = new UserCounter();
        userCounter.setAddTime(new Date());
        userCounter.setCounter(result.size());
        UserCounter saveResult = userCounterDao.save(userCounter);
        log.info("写入数据saveResult:{}",saveResult);
        log.info("执行定时任务，结束");
    }
}
