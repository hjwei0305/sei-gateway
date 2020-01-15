package com.ecmp.apigateway.dao;

import com.ecmp.apigateway.entity.GatewayInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GatewayInterfaceDao extends JpaRepository<GatewayInterface,String> {

    List<GatewayInterface> findByDeletedFalseAndValidateTokenFalseAndDeletedFalseAndApplicationCode(String appCode);

}
