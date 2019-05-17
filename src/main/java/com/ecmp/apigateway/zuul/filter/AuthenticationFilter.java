package com.ecmp.apigateway.zuul.filter;

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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.FORWARD_TO_KEY;

/**
 * 实现功能：
 * 身份认证过滤器
 *
 * @author 马超(Vision.Mac)
 * @version 1.0.00  2019-05-17 14:59
 */
@Slf4j
@Component
public class AuthenticationFilter extends ZuulFilter {
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_TOKEN = "Authorization";
    private static final String HEADER_SID = "_s";

    private static final String REDIS_KEY_JWT = "jwt:";

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private InterfaceService interfaceService;

    /**
     * to classify a filter by type. Standard types in Zuul are "pre" for pre-routing filtering,
     * "route" for routing to an origin, "post" for post-routing filters, "error" for error handling.
     * We also support a "static" type for static responses see  StaticResponseFilter.
     * Any filterType made be created or added and run by calling FilterProcessor.runFilters(type)
     *
     * @return A String representing that type
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * filterOrder() must also be defined for a filter. Filters may have the same  filterOrder if precedence is not
     * important for a filter. filterOrders do not need to be sequential.
     *
     * @return the int order of a filter
     */
    @Override
    public int filterOrder() {
        return 2;
    }

    /**
     * a "true" return from this method means that the run() method should be invoked
     *
     * @return true if the run() method should be invoked. false will not invoke the run() method
     */
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String uri = ctx.getRequest().getServletPath();

        if (StringUtils.containsIgnoreCase(uri, "sso")) {
            // @see org.springframework.cloud.netflix.zuul.filters.route.SendForwardFilter
//            ctx.put(FORWARD_TO_KEY, "http://127.0.0.1:8081/sso/login");

            return false;
        } else {

            Boolean checkToken = interfaceService.checkToken(uri);
            log.info("uri: {}, 是否需要Token检查: {}", uri, checkToken);

            return checkToken;
        }
    }

    /**
     * 拦截所有请求，并进行jwt认证，jwt默认反正Authorization里面
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
        boolean isToken = true;
        if (StringUtils.isBlank(token)) {
            isToken = false;
            String sid = request.getHeader(HEADER_SID);
            if (StringUtils.isBlank(sid)) {
                sid = request.getParameter(HEADER_SID);
                if (StringUtils.isBlank(sid)) {
                    Cookie[] cookies = request.getCookies();
                    if (cookies != null && cookies.length > 0) {
                        for (Cookie cookie : cookies) {
                            if (StringUtils.equals(HEADER_SID, cookie.getName())) {
                                sid = cookie.getValue();
                                break;
                            }
                        }
                    }
                }
            }

            if (StringUtils.isNotBlank(sid)) {
                token = redisTemplate.opsForValue().get(REDIS_KEY_JWT + sid);
            }
        }
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
                } else {
                    //header中设置token
                    if (!isToken) {
                        ctx.addZuulRequestHeader(HEADER_TOKEN, token);
                    }
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
