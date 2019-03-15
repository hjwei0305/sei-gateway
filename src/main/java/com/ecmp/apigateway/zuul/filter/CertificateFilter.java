package com.ecmp.apigateway.zuul.filter;

import com.ecmp.apigateway.manager.entity.GatewayInterface;
import com.ecmp.apigateway.manager.entity.common.ResponseModel;
import com.ecmp.apigateway.zuul.service.InterfaceService;
import com.ecmp.context.ContextUtil;
import com.ecmp.util.JsonUtils;
import com.ecmp.vo.SessionUser;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
@Slf4j
public class CertificateFilter extends ZuulFilter {

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";
//    private static final String APP_ID = "appId";

    //    @Autowired
//    private ZKService zkService;
    @Autowired
    private InterfaceService interfaceService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String uri = ctx.getRequest().getServletPath();

        GatewayInterface interfaces = interfaceService.getInterfaceByUri(uri);
        log.info("获取interfaces 成功，interfaces is {},uri is {}", interfaces, uri);
        if (interfaces == null) {
            return false;
        }
        return interfaces.getValidateToken();
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
        if (isSuccess != null && !((Boolean) isSuccess)) {
            return null;
        }
        //默认的浏览器权限请求头 如:Authorization:Bearer token
        HttpServletRequest request = ctx.getRequest();
        String authorization = request.getHeader(HEADER_STRING);
        log.info("Access Token is", authorization);
        if (StringUtils.isBlank(authorization)) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            log.error("Authorization 为空");
            ctx.setResponseBody(JsonUtils.toJson(ResponseModel.SESSION_INVALID()));
            ctx.set("isSuccess", false);
            return null;
        }
        try {
            SessionUser sessionUser = ContextUtil.getSessionUser(authorization);
            log.info("SessionUser is", sessionUser);
            if (sessionUser.isAnonymous()) {
                ctx.setSendZuulResponse(false);
                ctx.setResponseStatusCode(401);
                log.error("Authorization 为空");
                ctx.setResponseBody(JsonUtils.toJson(ResponseModel.SESSION_INVALID()));
                ctx.set("isSuccess", false);
                return null;
            }
        } catch (Exception ex) {
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
