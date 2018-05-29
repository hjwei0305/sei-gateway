package com.ecmp.apigateway.zuul.filter;

import com.ecmp.apigateway.model.GatewayInterface;
import com.ecmp.apigateway.model.common.ResponseModel;
import com.ecmp.apigateway.zuul.service.InterfaceService;
import com.ecmp.util.JsonUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * usage:接口是否转发过滤器
 * <p>
 * </p>
 * User:liusonglin; Date:2018/5/18;ProjectName:api-gateway;
 */
@Component
@Log4j
public class InterfaceAccessFilter extends ZuulFilter {

    @Autowired
    private InterfaceService interfaceService;

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
        boolean shouldFilter = false;
        List<GatewayInterface> interfaces = interfaceService.getInvalidInterface();
        RequestContext ctx = RequestContext.getCurrentContext();
        String uri = ctx.getRequest().getRequestURI();
        for(GatewayInterface item : interfaces){
            shouldFilter = uri.contains(item.getInterfaceURI());
            if(shouldFilter) return shouldFilter;
        }
        return shouldFilter;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(404);
        ctx.setResponseBody(JsonUtils.toJson(ResponseModel.NOT_FOUND("gateway.interface.deny")));
        ctx.set("isSuccess", false);
        return null;
    }


}
