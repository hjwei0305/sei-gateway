package com.ecmp.apigateway.web;

import com.ecmp.apigateway.enums.OperationTypeEnum;
import com.ecmp.apigateway.model.*;
import com.ecmp.apigateway.model.common.PageModel;
import com.ecmp.apigateway.model.common.ResponseModel;
import com.ecmp.apigateway.model.common.SearchParam;
import com.ecmp.apigateway.model.common.StaticVariable;
import com.ecmp.apigateway.service.IGatewayApplicationService;
import com.ecmp.apigateway.utils.RandomUtil;
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

    @RequestMapping("/show")
    public String show() {
        return "main/ApplicationView";
    }

    /**
     * 新增网关应用
     *
     * @param gatewayApplication 网关应用实体参数
     * @return
     */
    @RequestMapping("/add_gateway_application")
    @ResponseBody
    public Object addGatewayApplication(GatewayApplication gatewayApplication) {
        //设置网关code
        gatewayApplication.setApplicationCode(RandomUtil.getUniqueCode());
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
    public Object checkGatewayApplicationName(@RequestParam(name = "applicationName") String applicationName, @RequestParam(name = "operationType") OperationTypeEnum operationType) {
        boolean result = this.gatewayApplicationService.checkApplicationName(applicationName, operationType);
        return ResponseModel.SUCCESS(result);
    }

    /**
     * 移除网关-应用
     *
     * @param id              ID
     * @param applicationCode 应用code
     */
    @RequestMapping("/remove_gateway_application")
    @ResponseBody
    public Object removeGatewayApplication(String id, String applicationCode) {
        this.gatewayApplicationService.removeGatewayApplication(id, applicationCode);
        return ResponseModel.SUCCESS();
    }

    /**
     * 修改网关-应用
     *
     * @param gatewayApplication
     * @return
     */
    @ResponseBody
    @RequestMapping("/modify_gateway_application")
    public Object modifyGatewayApplication(GatewayApplication gatewayApplication) {
        this.gatewayApplicationService.modifyGatewayApplication(gatewayApplication);
        return ResponseModel.SUCCESS();
    }

    /**
     * 查询网关应用列表
     *
     * @param weatherPage 是否分页查询 true：分页 false：不分页
     * @param searchParam 查询参数对象
     * @return
     */
    @RequestMapping("/find_gateway_applications")
    @ResponseBody
    public Object findGatewayApplications(@RequestParam(value = "weatherPage", defaultValue = "1") boolean weatherPage, SearchParam searchParam) {
        if (weatherPage) {
            Page<GatewayApplication> result = this.gatewayApplicationService.findAllByKeywordAndPage(searchParam);
            return new PageModel<>(result);
        } else {
            List<GatewayApplication> result = this.gatewayApplicationService.findAll();
            return ResponseModel.SUCCESS(result);
        }
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
    @ResponseBody
    public Object findGatewayApplication(Model model, String id, String applicationCode) {
        GatewayApplication gatewayApplication = this.gatewayApplicationService.findGatewayApplicationByIdOrCode(id, applicationCode);
        /*model.addAttribute(StaticVariable.STATUS, HttpStatus.OK.value());
        model.addAttribute(StaticVariable.MESSAGE, StaticVariable.SUCCESS_MESSAGE);
        model.addAttribute(StaticVariable.DATA, gatewayApplication);*/
        return ResponseModel.SUCCESS(gatewayApplication);
    }

}
