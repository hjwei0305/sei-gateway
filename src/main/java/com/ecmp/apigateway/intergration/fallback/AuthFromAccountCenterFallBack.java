package com.ecmp.apigateway.intergration.fallback;

import com.ecmp.apigateway.entity.ResultData;
import com.ecmp.apigateway.intergration.AuthFromAccountCenter;

import org.springframework.stereotype.Component;


@Component
public class  AuthFromAccountCenterFallBack implements AuthFromAccountCenter {


    @Override
    public ResultData<String> getAnonymousToken() {
        return new ResultData<>(Boolean.FALSE, "请求账户中心失败", null);
    }

    @Override
    public ResultData<String> check(String sid) {
        return ResultData.fail("请求账户中心失败");
    }

}