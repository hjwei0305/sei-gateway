package com.ecmp.apigateway.controller;

import com.ecmp.apigateway.entity.GatewayInterface;
import com.ecmp.apigateway.entity.ResultData;
import com.ecmp.apigateway.service.InterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interface")
public class InterfaceController {

    @Autowired
    private InterfaceService interfaceService;

    @GetMapping("/findByAppCode")
    public ResultData<List<GatewayInterface>> findByAppCode(@RequestParam("appCode")String appCode){
        List<GatewayInterface> result = interfaceService.findByAppCode(appCode);
        return ResultData.success(result);
    }

    @PostMapping("/save")
    public ResultData<GatewayInterface> save(@RequestBody GatewayInterface gatewayInterface){
        gatewayInterface = interfaceService.save(gatewayInterface);
        return ResultData.success(gatewayInterface);
    }

    @DeleteMapping("/delete")
    public ResultData<Boolean> delete(String id){
        interfaceService.delete(id);
        return ResultData.success(true);
    }

    @GetMapping("reloadCache")
    public ResultData<Boolean> reloadCache(){
        return ResultData.success(interfaceService.reloadCache());
    }
}
