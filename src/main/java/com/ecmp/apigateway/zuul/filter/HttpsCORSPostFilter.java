package com.ecmp.apigateway.zuul.filter;

import com.ecmp.context.ContextUtil;
import com.ecmp.vo.SessionUser;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class HttpsCORSPostFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        //// 优先级为0，数字越大，优先级越低
        return 1;
    }
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        //过滤各种POST请求
        if(request.getMethod().equals(RequestMethod.OPTIONS.name())){
            return false;
        }
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse response = ctx.getResponse();
        HttpServletRequest request = ctx.getRequest();
        String uri = request.getServletPath();
        log.debug("*****************PostFilter run start*****************");

        response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Expose-Headers","X-forwared-port, X-forwarded-host");
        response.setHeader("Vary","Origin,Access-Control-Request-Method,Access-Control-Request-Headers");
        //允许继续路由
        ctx.setSendZuulResponse(true);
        ctx.setResponseStatusCode(200);
        log.debug("*****************PostFilter run end*****************");

        SessionUser user = ContextUtil.getSessionUser();
        log.debug("url {}, 当前用户{}", uri, user);
        Cookie cookie = new Cookie(ContextUtil.REQUEST_TOKEN_KEY, user.getSessionId());
        response.addCookie(cookie);

        return null;
    }
}
