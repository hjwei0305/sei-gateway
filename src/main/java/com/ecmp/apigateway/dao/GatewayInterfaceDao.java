package com.ecmp.apigateway.dao;

import com.ecmp.apigateway.entity.GatewayInterface;
import com.ecmp.core.dao.BaseEntityDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author:wangdayin
 * @date:2018/4/24
 * @remark:网关-接口dao层
 */
@Repository
public interface GatewayInterfaceDao extends BaseEntityDao<GatewayInterface> {

    /**
     * 根据应用code查询接口信息
     *
     * @param applicationCode 应用code
     * @param interfaceName   接口名称
     * @return
     */
    @Query(value = "SELECT g from GatewayInterface g where g.deleted=FALSE AND g.applicationCode = :applicationCode and (g.interfaceURI like :interfaceURI or g.interfaceName like :interfaceName)")
    Page<GatewayInterface> findByDeletedInterface
    (@Param("applicationCode") String applicationCode, @Param("interfaceName") String interfaceName, @Param("interfaceURI") String interfaceURI, Pageable pageable);

    /**
     * =
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

    /**
     * 分页可用接口集合-带参数查询
     *
     * @param applicationCode
     * @param likeKeywords
     * @param likeKeywords1
     * @param pageable
     * @return
     */
    @Query(value = "SELECT g from GatewayInterface g where g.deleted=FALSE AND isValid=true AND g.applicationCode = ?1 and (g.interfaceURI like ?3 or g.interfaceName like ?2)")
    Page<GatewayInterface> findByDeletedFalseAndIsValidTrueAndApplicationCodeAndInterfaceNameLikeOrInterfaceURILike(String applicationCode, String likeKeywords, String likeKeywords1, Pageable pageable);

    /***
     *      分页可用接口集合-不带参数查询
     * @param applicationCode
     * @param pageable
     * @return
     */
    Page<GatewayInterface> findByDeletedFalseAndIsValidTrueAndApplicationCode(String applicationCode, Pageable pageable);

    /***
     *     可用接口集合-不带参数查询
     * @param applicationCode
     * @return
     */
    List<GatewayInterface> findByDeletedFalseAndIsValidTrueAndApplicationCode(String applicationCode);

    /**
     * 通过uri获取接口
     *
     * @param uri
     * @return
     */
    @Query("select gi from GatewayInterface gi \n" +
            "left join gi.gatewayApiService gs where (:uri = gi.interfaceURI)" +
            "and :path = REPLACE(gs.servicePath,'**','')")
    GatewayInterface getInterfaceByUri(@Param("path") String path, @Param("uri") String uri);
}
