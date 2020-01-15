package com.ecmp.apigateway.intergration.fallback;

import com.ecmp.apigateway.entity.ResultData;
import com.ecmp.apigateway.intergration.AuthFromAccountCenter;

import org.springframework.stereotype.Component;


@Component
public class  AuthFromAccountCenterFallBack implements AuthFromAccountCenter {


    @Override
    public ResultData<String> getAnonymousToken() {
        String defaultToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2NTQzMjEiLCJpcCI6IlVua25vd24iLCJ1c2VyTmFtZSI6Iuezu-e7n-euoeeQhuWRmCIsImxvY2FsZSI6InpoX0NOIiwidXNlcklkIjoiMTU5MkQwMTItQTMzMC0xMUU3LUE5NjctMDI0MjBCOTkxNzlFIiwicmFuZG9tS2V5IjoiRjhGODRDMzktMzc3My0xMUVBLUIyQUMtMDI0MkMwQTg0NDIwIiwiYXV0aG9yaXR5UG9saWN5IjoiVGVuYW50QWRtaW4iLCJsb2dpblRpbWUiOjE1NzkwNzgxODI2MDEsImxvZ291dFVybCI6bnVsbCwiYXBwSWQiOiJCODAzMzBCNC0zQTBDLTZCM0EtNTQ3Ny03M0EwNjdFQTkzQ0MiLCJ1c2VyVHlwZSI6IkVtcGxveWVlIiwiZXhwIjoxNTc5MTA3MjgyLCJpYXQiOjE1NzkwNzgxODIsInRlbmFudCI6IjEwMDQ0IiwiYWNjb3VudCI6IjY1NDMyMSIsImVtYWlsIjoieXVud2VuLmRlbmdAY2hhbmdob25nLmNvbSJ9.2TwgkOSaZd1guDqtxc-LJCFKvDxtNzcY_w4LAKDPNWD7xV2ybOhY6N6gTMTJuvaCL_2dA0H_iCuwwhUrEBZcfg";
        return ResultData.success(defaultToken);
    }

    @Override
    public ResultData<String> check(String sid) {
        return ResultData.fail("请求账户中心失败");
    }

}