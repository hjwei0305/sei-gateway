package com.ecmp.apigateway;

import com.ecmp.apigateway.service.IGatewayApiAppClient;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: hejun
 * @date: 2018/4/26
 * @remark:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiAppServerTests {
    @Autowired
    private IGatewayApiAppClient gatewayApiAppClient;

    @Test
    @Ignore
    public void test() {
        Object result = this.gatewayApiAppClient.findAllApiApp();
        System.out.println("=======>" + result.toString());
    }

}
