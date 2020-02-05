package com.changhong.sei.apigateway.dao;

import com.changhong.sei.apigateway.entity.GatewayInterface;
import com.changhong.sei.core.dao.BaseEntityDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GatewayInterfaceDao extends BaseEntityDao<GatewayInterface> {

    List<GatewayInterface> findByDeletedFalseAndDeletedFalseAndApplicationCode(String appCode);

}
