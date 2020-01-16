package com.ecmp.apigateway.service;

import com.ecmp.apigateway.dao.GatewayInterfaceDao;
import com.ecmp.apigateway.entity.GatewayInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


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
    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    public Boolean checkToken(String uri) {
        return Objects.isNull(redisTemplate.opsForValue().get(key(uri)));
    }

    /**
     * 加载不需要做认证检查的接口到redis中
     */
    public void loadRuntimeData() {
        List<GatewayInterface> interfaceList = gatewayInterfaceDao.findAll();
        if (CollectionUtils.isEmpty(interfaceList)) {
            log.warn("未加载到接口数据");
            return;
        }
        interfaceList.forEach(gi -> {
            if(!gi.getValidateToken()){
                String keyTemplate = gi.getInterfaceURI().startsWith("/")?gi.getInterfaceURI():"/"+ gi.getInterfaceURI();
                redisTemplate.opsForValue().set(key(keyTemplate), "0");
            }
        });
    }

    private String key(String uri) {
        return "Gateway:NoToken" + uri.replaceAll("/", ":");
    }

    public List<GatewayInterface> findByAppCode(String appCode) {
        return gatewayInterfaceDao.findByDeletedFalseAndDeletedFalseAndApplicationCode(appCode);
    }

    public GatewayInterface save(GatewayInterface gi) {
        gi = gatewayInterfaceDao.save(gi);
        if(!gi.getValidateToken()){
            redisTemplate.opsForValue().set(key("/" + gi.getInterfaceURI()), "0");
        }else {
            redisTemplate.delete(key("/" + gi.getInterfaceURI()));
        }
        return gi;
    }

    public void delete(String id) {
        Optional<GatewayInterface> gi = gatewayInterfaceDao.findById(id);
        if(gi.isPresent()){
            gi.get().setDeleted(true);
            gatewayInterfaceDao.save(gi.get());
            redisTemplate.delete(key(gi.get().getInterfaceURI()));
        }
    }

    public Boolean reloadCache() {
        Set<String> gatewayKeys = redisTemplate.keys("Gateway:NoToken*");
        redisTemplate.delete(gatewayKeys);
        this.loadRuntimeData();
        return true;
    }
}
