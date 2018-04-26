package com.ecmp.apigateway;

import com.ecmp.apigateway.dao.GatewayApplicationDao;
import com.ecmp.apigateway.model.GatewayApplication;
import com.ecmp.apigateway.model.SearchParam;
import com.ecmp.apigateway.service.IGatewayApplicationService;
import com.ecmp.apigateway.utils.RandomUtil;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiGatewayApplicationTests {

    @Autowired
    private GatewayApplicationDao gatewayApplicationDao;
    @Autowired
    private IGatewayApplicationService gatewayApplicationService;

    @Test
    @Ignore
    public void contextLoads() {
    }

    @Test
    @Ignore
    public void testApplicationFindAll() {
        List<GatewayApplication> allications = this.gatewayApplicationDao.findByDeletedFalse();
        System.out.println(allications.size());
    }

    @Test
    @Ignore
    public void testAdd() {
        GatewayApplication gatewayApplication = new GatewayApplication(RandomUtil.getUniqueCode(), "测试应用", "test");
        this.gatewayApplicationService.addGatewayApplication(gatewayApplication);
    }

    @Test
    @Ignore
    public void testRemove() {
        this.gatewayApplicationService.removeGatewayApplication("31d7ce63-07c3-405d-b61d-75f21789ff91", null);
    }

    @Test
    @Ignore
    public void testUpdate() {
        GatewayApplication application = this.gatewayApplicationService.findGatewayApplicationByIdOrCode("31d7ce63-07c3-405d-b61d-75f21789ff91", null);
        application.setApplicationName("测试修改");
        this.gatewayApplicationService.modifyGatewayApplication(application);
    }

    @Test
    @Ignore
    public void testFind() {
        SearchParam searchParam = new SearchParam();
        searchParam.setKeywords("%测试%");
        Page<GatewayApplication> all1 = this.gatewayApplicationService.findAllByKeywordAndPage(searchParam);
        List<GatewayApplication> content = all1.getContent();
        System.out.println(content.size());
    }


}
