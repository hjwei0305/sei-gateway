package com.ecmp.apigateway.zuul.filter;

import com.ecmp.apigateway.utils.HttpUtils;
import com.ecmp.context.ContextUtil;
import com.ecmp.vo.SessionUser;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 扩展过滤器。用于将当前会话信息写入cookie
 */
@Component
@Slf4j
public class CustomPostFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        //// 优先级为1，数字越大，优先级越低
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse response = ctx.getResponse();
        HttpServletRequest request = ctx.getRequest();
        String uri = request.getServletPath();

        SessionUser user = ContextUtil.getSessionUser();
        log.debug("PostFilter run end url {}, 当前用户{}", uri, user);
        if (!user.isAnonymous()) {
            HttpUtils.writeCookieValue(user.getSessionId(), request, response);
        }
        return null;
    }

}
