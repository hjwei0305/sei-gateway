package com.ecmp.apigateway.intergration.fallback;

import com.ecmp.apigateway.entity.ResultData;
import com.ecmp.apigateway.intergration.AuthFromAccountCenter;

import org.springframework.stereotype.Component;


@Component
public class  AuthFromAccountCenterFallBack implements AuthFromAccountCenter {


    @Override
    public ResultData<String> getAnonymousToken() {
        return ResultData.fail("请求账户中心内部令牌失败");
    }

    @Override
    public ResultData<String> check(String sid) {
        return ResultData.fail("请求账户中心认证检查失败");
    }

}