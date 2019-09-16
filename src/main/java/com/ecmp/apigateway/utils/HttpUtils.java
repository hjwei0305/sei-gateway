package com.ecmp.apigateway.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.ecmp.context.ContextUtil;
import io.netty.handler.codec.http.cookie.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
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


    public static <T> T postFuc(String url, Map<String, String> params, TypeReference<T> typeReference) {
        HttpPost post = new HttpPost(url);
        List<NameValuePair> nvps = new ArrayList<>();
        for (Map.Entry<String, String> item : params.entrySet()) {
            nvps.add(new BasicNameValuePair(item.getKey(), item.getValue()));
        }
        CloseableHttpResponse response = null;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            post.setEntity(new UrlEncodedFormEntity(nvps));
            response = client.execute(post);
            String result = EntityUtils.toString(response.getEntity());
            return JSON.parseObject(result, typeReference);
        } catch (UnsupportedEncodingException e) {
            log.error("post set entity error", e);
        } catch (IOException e) {
            log.error("post error", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("关闭response出错");
                }
            }
        }
        return null;
    }

    public static <T> T getFun(String url, TypeReference<T> typeReference) {
        return getFun(url, null, typeReference);
    }

    public static <T> T getFun(String url, Map<String, String> params, TypeReference<T> typeReference) {
        log.info("get url is {}", url);
        URIBuilder builder = null;
        String result = null;
        CloseableHttpResponse response = null;
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            builder = new URIBuilder(url);
            if (params != null) {
                for (Map.Entry<String, String> item : params.entrySet()) {
                    builder.setParameter(item.getKey(), item.getValue());
                }
            }
            HttpGet get = new HttpGet(builder.build());
            response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity());
                return JSON.parseObject(result, typeReference);
            }
        } catch (URISyntaxException e) {
            log.error("builder url error", e);
        } catch (JSONException ex) {
            log.error("json 转换出错,{}", ex);
            if (typeReference.getType().getTypeName().equals("String")) {
                return (T) result;
            }
        } catch (IOException e) {
            log.error("json转换io出错", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("关闭response出错");
                }
            }
        }
        return null;
    }

    /**
     * 写cookie，base64编码
     */
    public static void writeCookieValue(String value, ServerHttpRequest request, ServerHttpResponse response) {
        byte[] encodedCookieBytes = Base64.getEncoder().encode(value.getBytes());
        String baseVal = new String(encodedCookieBytes);

        MultiValueMap<String, HttpCookie> sessionCookie = request.getCookies();
        ResponseCookie responseCookie = ResponseCookie.from(ContextUtil.REQUEST_TOKEN_KEY, baseVal)
                .maxAge(-1)
                .path("/")
                .httpOnly(true)
                .build();
        //设置Cookie最大生存时间,以秒为单位,负数的话为浏览器进程,关闭浏览器Cookie消失
        response.addCookie(responseCookie);
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
