package com.ecmp.apigateway.web;

import com.ecmp.apigateway.enums.InterfaceProtocolEnum;
import com.ecmp.apigateway.enums.OperationTypeEnum;
import com.ecmp.apigateway.model.GatewayInterface;
import com.ecmp.apigateway.model.PageModel;
import com.ecmp.apigateway.model.ResponseModel;
import com.ecmp.apigateway.model.SearchParam;
import com.ecmp.apigateway.service.IGatewayInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

/**
 * @author:wangdayin
 * @date:2018/4/25
 * @remark:
 */
@Controller
@RequestMapping("gateway_interface")
public class GatewayInterfaceController {
    @Autowired
    private IGatewayInterfaceService gatewayInterfaceService;

    @RequestMapping("/show")
    public String show() {
        return "main/InterfaceView";
    }

    /**
     * 新增网关应用接口
     *
     * @param gatewayInterface 接口对象
     * @return
     */
    @RequestMapping("add_gateway_interface")
    @ResponseBody
    public ResponseModel addGatewayInterface(GatewayInterface gatewayInterface) {
        this.gatewayInterfaceService.addGatewayInterface(gatewayInterface);
        return ResponseModel.SUCCESS();
    }

    /**
     * 移除网关接口信息
     *
     * @param id ID
     * @return
     */
    @RequestMapping("remove_gateway_interface")
    @ResponseBody
    public ResponseModel removeGatewayInterface(@RequestParam String id) {
        this.gatewayInterfaceService.removeGatewayInterface(id);
        return ResponseModel.SUCCESS();
    }

    /**
     * 修改网关接口信息
     *
     * @param gatewayInterface 接口信息
     * @return
     */
    @RequestMapping("modify_gateway_interface")
    @ResponseBody
    public ResponseModel modifyGatewayInterface(GatewayInterface gatewayInterface) {
        this.gatewayInterfaceService.modifyGatewayInterface(gatewayInterface);
        return ResponseModel.SUCCESS();
    }

    /**
     * 根据id查询网关接口信息
     *
     * @param id ID
     * @return
     */
    @RequestMapping("find_gateway_interface_id")
    @ResponseBody
    public ResponseModel findGatewayInterfaceById(@RequestParam String id) {
        GatewayInterface gatewayInterface = this.gatewayInterfaceService.findGatewayInterfaceById(id);
        return ResponseModel.SUCCESS(gatewayInterface);
    }

    /**
     * 查询网关接口信息 根据应用code，接口名称，接口uri地址
     *
     * @param applicationCode 应用code
     * @param interfaceName   接口名称
     * @param interfaceURI    接口地址
     * @return
     */
    @RequestMapping("check_gateway_interface")
    @ResponseBody
    public ResponseModel findGatewayInterface(@RequestParam String applicationCode, String interfaceName, String interfaceURI, @RequestParam(name = "operationType") OperationTypeEnum operationType) {
        boolean result = this.gatewayInterfaceService.checkGatewayInterface(applicationCode, interfaceName, interfaceURI, operationType);
        return ResponseModel.SUCCESS(result);
    }

    /**
     * 查询网关接口信息
     *
     * @param applicationCode 应用code
     * @param weatherPage     是否分页 true:分页 false：不分页
     * @param searchParam     查询参数
     * @return
     */
    @RequestMapping("find_gateway_interfaces")
    @ResponseBody
    public Object findGatewayInterfaces(@RequestParam String applicationCode, @RequestParam(value = "weatherPage", defaultValue = "1") boolean weatherPage, SearchParam searchParam) {
        if (weatherPage) {
            Page<GatewayInterface> result = this.gatewayInterfaceService.findGatewayInterfaceByPage(applicationCode, searchParam);
            return new PageModel<>(result);
        } else {
            List<GatewayInterface> result = this.gatewayInterfaceService.findGatewayInterfaceByNoPage(applicationCode);
            return ResponseModel.SUCCESS(result);
        }
    }

    /**
     * 查询接口协议枚举
     *
     * @return
     */
    @RequestMapping("find_protocol_enum")
    @ResponseBody
    public ResponseModel findProtocolEnum() {
        List<InterfaceProtocolEnum> interfaceProtocolEnums = Arrays.asList(InterfaceProtocolEnum.values());
        return ResponseModel.SUCCESS(interfaceProtocolEnums);
    }

}
