package com.ecmp.apigateway.zuul.filter;

import com.ecmp.apigateway.model.common.ResponseModel;
import com.ecmp.apigateway.model.vo.JwtVo;
import com.ecmp.util.JsonUtils;
import com.ecmp.util.RSAUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * usage:认证过滤器
 * <p>
 * </p>
 * User:liusonglin; Date:2018/5/18;ProjectName:api-gateway;
 */
@Slf4j
@Component
public class CertificateFilter extends ZuulFilter {

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";

    @Value("${certification.center.host}")
    private String certificationCenterHost;

    @Value("${certification.center.url}")
    private String certificationCenterUrl;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return false;
    }

    /***
     * 拦截所有请求，并进行jwt认证，jwt默认反正Authorization里面
     *
     * @return
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        //默认的浏览器权限请求头 如:Authorization:Bearer token
        String authorization = ctx.getRequest().getHeader(HEADER_STRING);
        if(StringUtils.isBlank(authorization)){
            log.error("没有权限认证信息");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(503);
            ctx.setResponseBody(JsonUtils.toJson(ResponseModel.ERROR()));
            return null;
        }
        String jwt = authorization.replace(TOKEN_PREFIX,"");
        JwtVo jwtVo = parseJwt(jwt);
        String result = getPublicKey(jwtVo.getAppId());
        Map<String,Object> resultMap = JsonUtils.fromJson(result, Map.class);
        String publicKey = String.valueOf(resultMap.get("publicKey"));
        String valJson = null;
        try {
            valJson = RSAUtils.decryptByPublicKey(jwtVo.getVal(),publicKey);
            ctx.addZuulRequestHeader("userInfo",valJson);
        } catch (Exception e) {
            log.error("公钥解密失败,error is {}",e);
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(503);
            ctx.setResponseBody(JsonUtils.toJson(ResponseModel.NOT_FOUND("公钥解密失败，系统无法认证")));
            return null;
        }
        return null;
    }


    /**
     * 通过appid 向认证中心获取该应用的公钥
     *
     * @param appId
     * @return
     */
    private String getPublicKey(String appId) {
        HttpGet get =new HttpGet(certificationCenterHost+certificationCenterUrl+"?appId="+appId);
        try(CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response =httpClient.execute(get)){
            return org.apache.http.util.EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            log.error("refresh error",e);
        }
        return null;
    }

    /***
     * 解析jwt
     *
     * @param jwt
     * @return
     */
    private JwtVo parseJwt(String jwt){
        try {
            JwtParser parser = Jwts.parser();

            Claims claims = parser.parseClaimsJwt(jwt).getBody();

            log.info("claims is {}",claims);

            String userInfo = claims.getSubject();

            return JsonUtils.fromJson(userInfo, JwtVo.class);
        }catch (ExpiredJwtException ex){
            log.error("token is expiration {}",ex);
            throw new JwtException("token is expiration");
        }catch (Exception ex){
            log.error("parser token exception {}",ex);
            throw new JwtException(ex.getMessage());
        }
    }
}
