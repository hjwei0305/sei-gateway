package com.changhong.sei.apigateway.service.client.fallback;

import com.changhong.sei.apigateway.service.client.AuthServiceClient;
import com.changhong.sei.core.dto.ResultData;
import org.springframework.stereotype.Component;

@Component
public class AuthFromAccountCenterFallBack implements AuthServiceClient {

    @Override
    public ResultData<String> getAnonymousToken() {
        return ResultData.success("请求账户中心内部令牌失败", AuthServiceClient.INTERNAL_ERROR);
    }

    @Override
    public ResultData<String> check(String sid) {
        return ResultData.success("请求账户中心认证检查失败", AuthServiceClient.INTERNAL_ERROR);
    }

}