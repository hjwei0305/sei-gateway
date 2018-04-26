package com.ecmp.apigateway.web;

import com.ecmp.apigateway.model.GatewayApiService;
import com.ecmp.apigateway.model.PageModel;
import com.ecmp.apigateway.model.ResponseModel;
import com.ecmp.apigateway.model.SearchParam;
import com.ecmp.apigateway.service.IGatewayApiAppClient;
import com.ecmp.apigateway.service.IGatewayApiServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * @author: hejun
 * @date: 2018/4/24
 * @remark: 应用服务-控制层跳转
 */
@Controller
@RequestMapping(value="/gateway_api_service")
public class GatewayApiServiceController {
    @Autowired
    private IGatewayApiServiceService gatewayApiServiceService;

    @Autowired
    private IGatewayApiAppClient gatewayApiAppClient;

    /**
     * 显示应用服务页面
     * @return
     */
    @RequestMapping("/show")
    public String show() {
        return "main/ApplicationServiceView";
    }
    /**
     * 获取配置中心应用服务
     * @return
     */
    @ResponseBody
    @RequestMapping("findAllApiApp")
    public Object findAllApiApp() {
        Object apiAppList = gatewayApiAppClient.findAllApiApp();
        return ResponseModel.SUCCESS(apiAppList);
    }

    /**
     * 新增
     * @param gatewayApiService
     * @return
     */
    @ResponseBody
    @RequestMapping("save")
    public Object save(GatewayApiService gatewayApiService) {
        gatewayApiServiceService.save(gatewayApiService);
        return ResponseModel.SUCCESS();
    }

    /**
     * 编辑
     * @param gatewayApiService
     * @return
     */
    @ResponseBody
    @RequestMapping("edit")
    public Object edit(GatewayApiService gatewayApiService) {
        gatewayApiServiceService.edit(gatewayApiService);
        return ResponseModel.SUCCESS();
    }

    /**
     * 删除所有
     * @return
     */
    @ResponseBody
    @RequestMapping("removeAll")
    public Object removeAll() {
        gatewayApiServiceService.removeAll();
        return ResponseModel.SUCCESS();
    }

    /**
     * 根据ID、应用IP删除
     * @param id ID
     * @param serviceAppId 应用ID
     * @return
     */
    @ResponseBody
    @RequestMapping("removeById")
    public Object removeById(String id, String serviceAppId) {
        gatewayApiServiceService.removeById(id, serviceAppId);
        return ResponseModel.SUCCESS();
    }

    /**
     * 查询所有
     * @return
     */
    @ResponseBody
    @RequestMapping("findAll")
    public Object findAll() {
        List<GatewayApiService> gatewayApiServiceList = gatewayApiServiceService.findAll();
        return ResponseModel.SUCCESS(gatewayApiServiceList);
    }

    /**
     * 分页查询
     * @param weatherPage 是否分页：true|false
     * @param searchParam 页面查询参数
     * @return
     */
    @ResponseBody
    @RequestMapping("findAllByPage")
    public Object findAllByPage(@RequestParam(value = "weatherPage", defaultValue = "1") boolean weatherPage, SearchParam searchParam) {
        if(weatherPage){
            Page<GatewayApiService> gatewayApiServicePage = gatewayApiServiceService.findAllByPage(searchParam);
            //return ResponseModel.SUCCESS(new PageModel<>(gatewayApiServicePage));
            return new PageModel<>(gatewayApiServicePage);
        } else {
            List<GatewayApiService> gatewayApiServiceList = gatewayApiServiceService.findAll();
            //return ResponseModel.SUCCESS(gatewayApiServiceList);
            return gatewayApiServiceList;
        }
    }

    /**
     * 根据ID、应用ID查询
     * @param id ID
     * @param serviceAppId 应用ID
     * @return
     */
    @ResponseBody
    @RequestMapping("findById")
    public Object findById(String id, String serviceAppId) {
        GatewayApiService gatewayApiService = gatewayApiServiceService.findById(id, serviceAppId);
        return ResponseModel.SUCCESS(gatewayApiService);
    }
}