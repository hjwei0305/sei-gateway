package com.ecmp.apigateway.config;

import com.ecmp.apigateway.exception.MessageRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author:wangdayin
 * @date:2018/4/25
 * @remark: 统一异常处理
 */
@ControllerAdvice
public class ExceptionHandle {
    /**
     * 异常统一处理
     * <p/>
     */
    @ExceptionHandler({MessageRuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ModelAndView processUnauthenticatedException(NativeWebRequest request, MessageRuntimeException e) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("message", e.getMessage());
        mv.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return mv;
    }
}
