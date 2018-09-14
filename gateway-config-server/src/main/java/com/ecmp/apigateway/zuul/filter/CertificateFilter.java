package com.ecmp.apigateway.zuul.filter;

import com.ecmp.apigateway.ZKService;
import com.ecmp.apigateway.model.common.ResponseModel;
import com.ecmp.util.JsonUtils;
import com.ecmp.util.JwtTokenUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.jsonwebtoken.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

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
    private ZKService zkService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

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
        RequestContext ctx = RequestContext.getCurrentContext();
        String uri = ctx.getRequest().getRequestURI();
        if(!uri.contains("login")){
            return true;
        }else{
            return false;
        }
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
            ctx.setResponseStatusCode(401);
            log.error("Authorization 为空");
            ctx.setResponseBody(JsonUtils.toJson(ResponseModel.SESSION_INVALID()));
            ctx.set("isSuccess", false);
            return null;
        }
        try{
            Claims claims = jwtTokenUtil.getClaimFromToken(authorization);
            if(claims != null){
                log.info("claims is",claims);
            }
        }catch (Exception ex){
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(403);
            log.error("jwt解析失败");
            ctx.setResponseBody(JsonUtils.toJson(ResponseModel.SESSION_INVALID()));
            ctx.set("isSuccess", false);
            return null;
        }
        return null;
    }
}
