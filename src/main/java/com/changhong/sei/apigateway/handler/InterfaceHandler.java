package com.changhong.sei.apigateway.handler;

import com.changhong.sei.apigateway.service.InterfaceService;
import com.changhong.sei.core.dto.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/interface")
public class InterfaceHandler {

    @Autowired
    private InterfaceService interfaceService;

    @GetMapping("reloadCache")
    public ResultData<Boolean> reloadCache(){
        return ResultData.success(interfaceService.reloadCache());
    }
}
