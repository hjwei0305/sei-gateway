package com.ecmp.apigateway.web;

import com.ecmp.apigateway.model.vo.MenuVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

/**
 * 实现功能：首页控制器
 *
 * @author cs
 * @version 1.0.00
 */
@Controller
public class HomeController {

    @RequestMapping("/index")
    public String index() {
        return "home/HomeView";
    }

    @RequestMapping("/homePage")
    public String homePageView() {
        return "home/HomePageView";
    }

    /**
     * 获取用户有权限的操作菜单树(VO)
     */
    @ResponseBody
    @RequestMapping(value = "/getMenus")
    public List<MenuVo> getMenus() {
        return Arrays.asList(
                new MenuVo("application", "/gateway_application/show", "应用", "配置【应用】", "ecmp-sys-default"),
                new MenuVo("interface", "/gateway_interface/show", "接口", "配置应用的【接口】", "ecmp-sys-default"),
                new MenuVo("applicationService", "/gateway_api_service/show", "应用服务", "配置应用的【应用服务】", "ecmp-sys-default")
        );
    }
}
