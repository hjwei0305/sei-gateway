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
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.nio.charset.Charset;

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
    private static final String HEADER_TOKEN = "Authorization";
    private static final String HEADER_SID = "_s";

    private static final String REDIS_KEY_JWT = "jwt:";
//    private static final String APP_ID = "appId";

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private InterfaceService interfaceService;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String uri = ctx.getRequest().getServletPath();
        System.out.println(ctx.getRequest().getContextPath());

        Boolean checkToken = interfaceService.checkToken(uri);
        log.info("uri: {}, 是否需要Token检查: {}", uri, checkToken);

        return checkToken;
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
        String uri = request.getServletPath();
        String token = request.getHeader(HEADER_TOKEN);
//        if (StringUtils.isBlank(token)) {
//            String sid = request.getHeader(HEADER_SID);
//            if (StringUtils.isBlank(sid)) {
//                sid = request.getParameter(HEADER_SID);
//                if (StringUtils.isBlank(sid)) {
//                    Cookie[] cookies = request.getCookies();
//                    if (cookies != null && cookies.length > 0) {
//                        for (Cookie cookie : cookies) {
//                            if (StringUtils.equals(HEADER_SID, cookie.getName())) {
//                                sid = cookie.getValue();
//                            }
//                        }
//                    }
//                }
//            }
//
//            if (StringUtils.isNotBlank(sid)) {
//                token = redisTemplate.opsForValue().get(REDIS_KEY_JWT + sid);
//            }
//        }
        log.info("Access Token is {}  uri {}", token, uri);
        if (StringUtils.isBlank(token)) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(ResponseModel.STATUS_ACCESS_UNAUTHORIZED);
            log.error("Authorization 为空  uri {}", uri);
            ctx.setResponseBody(JsonUtils.toJson(ResponseModel.UNAUTHORIZED("Authorization 为空")));
            ctx.set("isSuccess", false);
            return null;
        }
        try {
            SessionUser sessionUser = ContextUtil.getSessionUser(token);
            log.info("SessionUser is {}  uri {}", sessionUser, uri);
            if (sessionUser.isAnonymous()) {
                ctx.setSendZuulResponse(false);
                ctx.setResponseStatusCode(ResponseModel.STATUS_ACCESS_UNAUTHORIZED);
                log.error("非法的token   uri {}", uri);
                ctx.setResponseBody(JsonUtils.toJson(ResponseModel.UNAUTHORIZED("非法的token")));
                ctx.set("isSuccess", false);
                return null;
            } else {
                String token1 = redisTemplate.opsForValue().get(REDIS_KEY_JWT + sessionUser.getSessionId());
                if (StringUtils.isBlank(token1) || !StringUtils.equals(token, token1)) {
                    ctx.setSendZuulResponse(false);
                    ctx.setResponseStatusCode(ResponseModel.STATUS_ACCESS_UNAUTHORIZED);
                    log.error("会话过期  uri {}", uri);
                    ctx.setResponseBody(JsonUtils.toJson(ResponseModel.UNAUTHORIZED("会话过期")));
                    ctx.set("isSuccess", false);
                    return null;
                }
            }
        } catch (Exception ex) {
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(ResponseModel.STATUS_ACCESS_UNAUTHORIZED);
            log.error("jwt解析失败  URI  {}", uri);
            ctx.setResponseBody(JsonUtils.toJson(ResponseModel.UNAUTHORIZED(ex.getMessage())));
            ctx.set("isSuccess", false);
            return null;
        }
        return null;
    }
}
