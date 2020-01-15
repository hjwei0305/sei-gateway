package com.ecmp.apigateway.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/14;ProjectName:mc-service;
 */
@Slf4j
public class HttpUtils {

    public static final String HEADER_TOKEN = "Authorization";
    public static final String HEADER_TOKEN_X = "X-Authorization";
    public static final String HEADER_SID = "_s";
    public static final String HEADER_IP = "clientIP";

    private HttpUtils() {
    }

    public static String getIpAddr(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        // 从nginx中获取ip
        String ip = headers.getFirst("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        return ip;
    }
    /**
     * 写cookie，base64编码
     */
    public static String readCookieValue(ServerHttpRequest request) {
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        HttpCookie sidCookies = cookies.getFirst(HEADER_SID);
        if(sidCookies != null){
            byte[] encodedCookieBytes = Base64.getDecoder().decode(sidCookies.getValue());
            return new String(encodedCookieBytes);
        }
        return null;
    }
}
