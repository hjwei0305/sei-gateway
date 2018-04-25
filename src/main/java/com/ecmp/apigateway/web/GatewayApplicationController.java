package com.ecmp.apigateway.web;

import com.ecmp.apigateway.model.GatewayApplication;
import com.ecmp.apigateway.model.ResponseModel;
import com.ecmp.apigateway.model.SearchParam;
import com.ecmp.apigateway.model.StaticVariable;
import com.ecmp.apigateway.service.IGatewayApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author:wangdayin
 * @date:2018/4/25
 * @remark: 网关-应用控制层
 */
@Controller
@RequestMapping("/gateway_application")
public class GatewayApplicationController {
    @Autowired
    private IGatewayApplicationService gatewayApplicationService;

    /**
     * 新增网关应用
     *
     * @param gatewayApplication 网关应用实体参数
     * @return
     */
    @RequestMapping("/add_gateway_application")
    @ResponseBody
    public Object addGatewayApplication(GatewayApplication gatewayApplication) {
        this.gatewayApplicationService.addGatewayApplication(gatewayApplication);
        return ResponseModel.SUCCESS();
    }

    /**
     * 根据名称查询网关应用
     *
     * @param applicationName 应用名称
     * @return
     */
    @RequestMapping("/check_application_name")
    @ResponseBody
    public Object checkGatewayApplicationName(@RequestParam(name = "applicationName") String applicationName) {
        GatewayApplication application = this.gatewayApplicationService.findGatewayApplicationByName(applicationName);
        return application;
    }

    /**
     * 移除网关-应用
     *
     * @param id              ID
     * @param applicationCode 应用code
     */
    @RequestMapping("/remove_gateway_application")
    @ResponseBody
    public void removeGatewayApplication(String id, String applicationCode) {
        this.gatewayApplicationService.removeGatewayApplication(id, applicationCode);
    }

    /**
     * 修改网关-应用
     *
     * @param gatewayApplication
     * @return
     */
    @RequestMapping("/modify_gateway_application")
    public String modifyGatewayApplication(GatewayApplication gatewayApplication) {
        this.gatewayApplicationService.modifyGatewayApplication(gatewayApplication);
        return "";
    }

    /**
     * 查询网关应用列表
     *
     * @param model
     * @param weatherPage 是否分页查询 true：分页 false：不分页
     * @param searchParam 查询参数对象
     * @return
     */
    @RequestMapping("/find_gateway_applications")
    public String findGatewayApplications(Model model, boolean weatherPage, SearchParam searchParam) {
        if (weatherPage) {
            Page<GatewayApplication> allApplications = this.gatewayApplicationService.findAllByKeywordAndPage(searchParam);
            model.addAttribute(StaticVariable.DATA, allApplications);
        } else {
            List<GatewayApplication> all = this.gatewayApplicationService.findAll();
            model.addAttribute(StaticVariable.DATA, all);
        }
        model.addAttribute(StaticVariable.STATUS, HttpStatus.OK.value());
        model.addAttribute(StaticVariable.MESSAGE, StaticVariable.SUCCESS_MESSAGE);
        return "";
    }

    /**
     * 根据id或者code查询网关应用
     *
     * @param model
     * @param id              ID
     * @param applicationCode 应用code
     * @return
     */
    @RequestMapping("/find_gateway_application")
    public String findGatewayApplication(Model model, String id, String applicationCode) {
        GatewayApplication gatewayApplication = this.gatewayApplicationService.findGatewayApplicationByIdOrCode(id, applicationCode);
        model.addAttribute(StaticVariable.STATUS, HttpStatus.OK.value());
        model.addAttribute(StaticVariable.MESSAGE, StaticVariable.SUCCESS_MESSAGE);
        model.addAttribute(StaticVariable.DATA, gatewayApplication);
        return "";
    }

}
