package com.ecmp.apigateway.service;

import com.ecmp.apigateway.dao.GatewayInterfaceDao;
import com.ecmp.apigateway.entity.GatewayInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


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
        return gatewayInterfaceDao.findByDeletedFalseAndValidateTokenFalseAndDeletedFalseAndApplicationCode(appCode);
    }

    public GatewayInterface save(GatewayInterface gi) {
        gi = gatewayInterfaceDao.save(gi);
        if(!gi.getValidateToken()){
            redisTemplate.opsForValue().set(key("/" + gi.getInterfaceURI()), "0");
        }
        return gi;
    }

    public void delete(String id) {
        Optional<GatewayInterface> gi = gatewayInterfaceDao.findById(id);
        if(gi.isPresent()){
            gatewayInterfaceDao.deleteById(id);
            redisTemplate.delete(key(gi.get().getInterfaceURI()));
        }
    }
}
