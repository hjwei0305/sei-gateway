package com.ecmp.apigateway.manager.web;

import com.ecmp.apigateway.manager.entity.GatewayApiService;
import com.ecmp.apigateway.manager.entity.common.PageModel;
import com.ecmp.apigateway.manager.entity.common.ResponseModel;
import com.ecmp.apigateway.manager.entity.common.SearchParam;
import com.ecmp.apigateway.manager.service.IGatewayApiServiceService;
import com.ecmp.apigateway.manager.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * author: hejun
 * date: 2018/4/24
 * remark: 应用服务-控制层跳转
 * update:liusonglin 把路由刷新放到控制层(数据库事物提交后)
 */
@Controller
@RequestMapping(value = "/gateway_api_service")
public class GatewayApiServiceController {
    @Autowired
    private IGatewayApiServiceService gatewayApiServiceService;


    @Autowired
    private InitService initService;

    /**
     * 显示应用服务页面
     * @return
     */
    @RequestMapping("/show")
    public Mono<String> show() {
        return Mono.create(monoSink -> monoSink.success("main/ApplicationServiceView"));
    }

    /**
     * 新增应用服务
     * @param gatewayApiService 应用服务-实体参数
     * @return
     */

    @ResponseBody
    @RequestMapping("save")
    public Object save(GatewayApiService gatewayApiService) {
        gatewayApiServiceService.save(gatewayApiService);
        return ResponseModel.SUCCESS();
    }

    /**
     * 通过appId刷新路由
     * @param appId
     * @return
     */
    @ResponseBody
    @RequestMapping("refreshByAppId")
    public Object refreshByAppId(String appId) {
        initService.initServiceByAppId(appId);
        gatewayApiServiceService.refresh();
        return ResponseModel.SUCCESS();
    }

    /**
     * 编辑应用服务
     * @param gatewayApiService 应用服务-实体参数
     * @return
     */
    @ResponseBody
    @RequestMapping("edit")
    public Object edit(GatewayApiService gatewayApiService) {
        gatewayApiServiceService.edit(gatewayApiService);
        gatewayApiServiceService.refresh();
        return ResponseModel.SUCCESS();
    }

    /**
     * 根据ID删除应用服务
     * @param id 主键ID
     * @return
     */
    @ResponseBody
    @RequestMapping("removeById")
    public Object removeById(String id) {
        gatewayApiServiceService.removeById(id);
        gatewayApiServiceService.refresh();
        return ResponseModel.SUCCESS();
    }

    /**
     * 分页查询应用服务
     * @param weatherPage 是否分页：true|false
     * @param searchParam 页面查询参数
     * @return
     */
    @ResponseBody
    @RequestMapping("findAllByPage")
    public Object findAllByPage(@RequestParam(value = "weatherPage", defaultValue = "1") boolean weatherPage, SearchParam searchParam) {
        if (weatherPage) {
            Page<GatewayApiService> gatewayApiServicePage = gatewayApiServiceService.findAllByPage(searchParam);
            return new PageModel<>(gatewayApiServicePage);
        } else {
            return gatewayApiServiceService.findAll();
        }
    }

    /**
     * 根据ID查询应用服务
     * @param id 主键ID
     * @return
     */
    @ResponseBody
    @RequestMapping("findById")
    public Object findById(String id) {
        GatewayApiService gatewayApiService = gatewayApiServiceService.findById(id);
        return ResponseModel.SUCCESS(gatewayApiService);
    }

    /**
     * 获取所有应用服务
     * @return
     */
    @ResponseBody
    @RequestMapping("config/findAllApiApp")
    public Object findAllApiApp() {
        Object apiApplications = gatewayApiServiceService.findAllApiApp();
        return ResponseModel.SUCCESS(apiApplications);
    }

    /**
     * 启用应用服务网关路由
     * @param id 主键ID
     * @return
     */
    @GetMapping("router/startById")
    @ResponseBody
    public Object startById(String id) {
        try {
            gatewayApiServiceService.enableById(id, true);
            gatewayApiServiceService.refresh();
            return ResponseModel.SUCCESS();
        }catch (Exception ex){
            gatewayApiServiceService.enableById(id, false);
            throw new RuntimeException("the router refresh error is "+ex.getMessage());
        }
    }

    /**
     * 停用应用服务网关路由
     * @param id 主键ID
     * @return
     */
    @ResponseBody
    @RequestMapping("router/stopById")
    public Object stopById(String id) {
        try {
            gatewayApiServiceService.enableById(id, false);
            gatewayApiServiceService.refresh();
            return ResponseModel.SUCCESS();
        } catch (Exception ex) {
            gatewayApiServiceService.enableById(id, true);
            throw new RuntimeException("the router refresh error is " + ex.getMessage());
        }
    }
}
