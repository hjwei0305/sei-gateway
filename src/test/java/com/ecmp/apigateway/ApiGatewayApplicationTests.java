package com.ecmp.apigateway;

import com.ecmp.apigateway.dao.GatewayApplicationDao;
import com.ecmp.apigateway.model.GatewayApplication;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiGatewayApplicationTests {

    @Autowired
    private GatewayApplicationDao gatewayApplicationDao;

    @Test
    @Ignore
    public void contextLoads() {
    }

    @Test
    @Ignore
    public void testApplicationFindAll() {
        List<GatewayApplication> allications = this.gatewayApplicationDao.findAllByDeletedFalse();
        System.out.println(allications.size());
    }


}
