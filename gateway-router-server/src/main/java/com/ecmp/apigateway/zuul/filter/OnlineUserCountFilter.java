package com.ecmp.apigateway.zuul.filter;

import com.ecmp.apigateway.model.vo.SessionUser;
import com.ecmp.apigateway.utils.RedisUtils;
import com.ecmp.util.JsonUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/20;ProjectName:api-gateway;
 */
@Component
@Slf4j
public class OnlineUserCountFilter extends ZuulFilter {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        //默认的浏览器权限请求头 如:Authorization:Bearer token
        HttpServletRequest request = ctx.getRequest();
        try {
            String json = request.getHeader("sessionUser");
            if (StringUtils.isNotBlank(json)) {
                json = new String(Base64.getDecoder().decode(json));
                SessionUser sessionUser = JsonUtils.fromJson(json, SessionUser.class);
                if (!"anonymous".equals(sessionUser.getUserId())) {
                    redisUtils.counterAdd(sessionUser);
                }
            }
        }catch (Exception ex){
            log.error("存储在线用户出错",ex);
        }
        return null;
    }
}
