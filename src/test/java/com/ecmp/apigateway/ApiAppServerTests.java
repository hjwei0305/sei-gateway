package com.ecmp.apigateway;

import com.ecmp.apigateway.service.IGatewayApiAppService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author:wangdayin
 * @date:2018/4/26
 * @remark:
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiAppServerTests {
    @Autowired
    private IGatewayApiAppService gatewayApiAppService;

    @Test
    @Ignore
    public void test() {
        String result = this.gatewayApiAppService.findAllApiApp();
        System.out.println("=======>" + result);
    }

}
