package com.ecmp.apigateway.dao;

import com.ecmp.apigateway.model.GatewayInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author:wangdayin
 * @date:2018/4/24
 * @remark:网关-接口dao层
 */
public interface GatewayInterfaceDao extends JpaRepository<GatewayInterface, String> {

    /**
     * 根据应用code查询接口信息
     *
     * @param applicationCode 应用code
     * @param interfaceName   接口名称
     * @param interfaceURI    接口URI
     * @return
     */
    Page<GatewayInterface> findByDeletedFalseAndApplicationCodeOrInterfaceNameLikeOrInterfaceURILike
    (@Param("applicationCode") String applicationCode, @Param("interfaceName") String interfaceName, @Param("interfaceURI") String interfaceURI, Pageable pageable);

    /**
     * 根据应用code查询接口信息
     *
     * @param applicationCode 应用code
     * @return
     */
    Page<GatewayInterface> findByDeletedFalseAndApplicationCode(String applicationCode, Pageable pageable);

    /**
     * 根据应用code，接口名称或者接口uri地址查询接口信息
     *
     * @param applicationCode 应用code
     * @param interfaceName   接口名称
     * @param interfaceURI    接口uri地址
     * @return
     */
    List<GatewayInterface> findByApplicationCodeAndInterfaceNameOrInterfaceURI(String applicationCode, String interfaceName, String interfaceURI);

    /**
     * 根据id查询接口信息
     *
     * @param id ID
     * @return
     */
    GatewayInterface findByDeletedFalseAndId(String id);

    /**
     * 根据应用code查询接口信息
     *
     * @param applicationCode 应用code
     * @return
     */
    List<GatewayInterface> findByDeletedFalseAndApplicationCode(String applicationCode);
}
