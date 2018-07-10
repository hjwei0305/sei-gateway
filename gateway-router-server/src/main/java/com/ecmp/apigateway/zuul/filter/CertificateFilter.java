package com.ecmp.apigateway.zuul.filter;

import com.ecmp.apigateway.ZKService;
import com.ecmp.apigateway.model.common.ResponseModel;
import com.ecmp.util.JsonUtils;
import com.ecmp.util.JwtTokenUtil;
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
import java.util.Date;
import java.util.Map;

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
    private static final String HEADER_STRING = "Authorization";
    private static final String APP_ID = "appId";

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private ZKService zkService;

//    @Value("${certification.center.url}")
//    private String certificationCenterUrl;

//    @Value("${certification.center.path}")
//    private String certificationCenterPath;

//    @Value("${default.public.key}")
//    private String defaultPublicKey;

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

        try {
            Date date = jwtTokenUtil.getExpirationDateFromToken(jwt);
            if (System.currentTimeMillis() > date.getTime()) {
                ctx.setSendZuulResponse(false);
                ctx.setResponseStatusCode(503);
                ctx.setResponseBody(JsonUtils.toJson(ResponseModel.NOT_FOUND("gateway.certification.error")));
                ctx.set("isSuccess", false);
                log.info("token过期");
                return null;
            }
//            ctx.addZuulRequestHeader("userInfo",valJson);
//            log.info("get userInfo is {}",valJson);
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
}
