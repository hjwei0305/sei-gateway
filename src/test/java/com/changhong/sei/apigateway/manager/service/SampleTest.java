package com.changhong.sei.apigateway.manager.service;

/**
 * @author Vision.Mac
 * @version 1.0.1 2019/3/15 10:10
 */
public class SampleTest {

    public static void main(String[] args) {
        String uri = "/auth-service/userAuth/checkAuth";
        System.out.println(uri.substring(0, uri.indexOf("/", 2) + 1));
        System.out.println(uri.substring(uri.indexOf("/", 2)));
        System.out.println(uri.replaceAll("/", ":"));

        System.out.println("/auth-service/**".replaceAll("[/|*]", ""));
    }
}
