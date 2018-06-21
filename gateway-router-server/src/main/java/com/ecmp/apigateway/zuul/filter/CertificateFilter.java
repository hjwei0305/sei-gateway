package com.ecmp.apigateway.zuul.filter;

import com.ecmp.apigateway.ZKService;
import com.ecmp.apigateway.model.common.ResponseModel;
import com.ecmp.util.JsonUtils;
import com.ecmp.util.RSAUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.jsonwebtoken.*;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Key;

/**
 * usage:认证过滤器
 * <p>
 * </p>
 * User:liusonglin; Date:2018/5/18;ProjectName:api-gateway;
 */
@Component
public class CertificateFilter extends ZuulFilter {
    private static final Logger log = LoggerFactory.getLogger(CertificateFilter.class);

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "ecmp-token";
    private static final String APP_ID = "appId";

    @Autowired
    private ZKService zkService;

    @Value("${certification.center.url}")
    private String certificationCenterUrl;

    @Value("${certification.center.path}")
    private String certificationCenterPath;

    @Value("${default.public.key}")
    private String defaultPublicKey;

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
        return true;
    }

    /***
     * 拦截所有请求，并进行jwt认证，jwt默认反正Authorization里面
     *
     * @return
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Object isSuccess = ctx.get("isSuccess");
        if(isSuccess !=null && !((Boolean)isSuccess)){
            return null;
        }
        //默认的浏览器权限请求头 如:Authorization:Bearer token
        HttpServletRequest request = ctx.getRequest();
        String authorization = request.getHeader(HEADER_STRING);
        if(StringUtils.isBlank(authorization)){
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(503);
            ctx.setResponseBody(JsonUtils.toJson(ResponseModel.ERROR("gateway.certification.empty")));
            return null;
        }
        String jwt = authorization.replace(TOKEN_PREFIX,"");
        String appId = request.getHeader(APP_ID);
        String result = getPublicKey(appId);
        String publicKey = defaultPublicKey;
//        if(result != null) {
//            Map<String, Object> resultMap = JsonUtils.fromJson(result, Map.class);
//            publicKey = String.valueOf(resultMap.get("publicKey"));
//        }
        String valJson = parseJWT(jwt,publicKey);
        try {
            ctx.addZuulRequestHeader("userInfo",valJson);
            log.info("get userInfo is {}",valJson);
            log.info("转发网址：{}",request.getRequestURL().toString());
        } catch (Exception e) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(503);
            ctx.setResponseBody(JsonUtils.toJson(ResponseModel.NOT_FOUND("gateway.certification.error")));
            ctx.set("isSuccess", false);
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
        //优先从系统环境变量中读取
        //优先从系统环境变量中读取
        HttpGet get=null;
        String apiGatewayAppId = System.getenv("ECMP_APP_ID");
        if (StringUtils.isNotBlank(apiGatewayAppId)) {
            String apiGatewayHost =zkService.getZookeeperData(apiGatewayAppId,"AUTH_CENTER","oauth2.base.url");
            if (StringUtils.isNotBlank(apiGatewayHost)) {
                get = new HttpGet(apiGatewayHost + certificationCenterPath + "?appId=" + appId);
            }
        }
        if(null == get) {
            get  =new HttpGet(certificationCenterUrl+certificationCenterPath+"?appId="+appId);
        }

        try(CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response =httpClient.execute(get)){
            return org.apache.http.util.EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            log.error("get public key by appId error",e);
        }
        return null;
    }

    public String parseJWT(String jwt,String publicKey){
        try {
            Key key = RSAUtils.getPublicKey(publicKey);


            JwtParser parser = Jwts.parser().setSigningKey(key);

            if(!parser.isSigned(jwt)){
                throw new JwtException("token must be signed");
            }

            Claims claims = parser.parseClaimsJws(jwt).getBody();

            log.info("claims",claims.getExpiration());

            return claims.getSubject();
        }catch (ExpiredJwtException ex){
            log.error("token is expiration {}",ex);
            throw new JwtException("token is expiration");
        }catch (Exception ex){
            log.error("parser token exception {}",ex);
            throw new JwtException(ex.getMessage());
        }
    }
}
