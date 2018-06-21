package com.ecmp.apigateway.model.vo;

import com.ecmp.enums.UserAuthorityPolicy;
import com.ecmp.enums.UserType;

import java.util.Date;
import java.util.Locale;

/**
 * usage:
 * <p>
 * </p>
 * User:liusonglin; Date:2018/6/20;ProjectName:api-gateway;
 */
public class SessionUser {

    private final static String defaultValue = "anonymous";

    /**
     * accessToken
     */
    private String accessToken;
    /**
     * 用户id，平台唯一
     */
    private String sessionId;
    /**
     * 用户id，平台唯一
     */
    private String userId = defaultValue;
    /**
     * 用户账号
     */
    private String account = defaultValue;
    /**
     * 用户名
     */
    private String userName = defaultValue;
    /**
     * 租户代码
     */
    private String tenantCode;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 用户类型
     */
    private UserType userType = UserType.Employee;
    /**
     * 用户权限策略
     */
    private UserAuthorityPolicy authorityPolicy = UserAuthorityPolicy.NormalUser;
    /**
     * 客户端IP
     */
    private String ip = "Unknown";
    /**
     * 语言环境
     */
    private Locale locale = Locale.getDefault();
    /**
     * 应用代码
     */
    private String appId = "Unknown";
    /**
     * 登录时间
     */
    private Date loginTime;


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserAuthorityPolicy getAuthorityPolicy() {
        return authorityPolicy;
    }

    public void setAuthorityPolicy(UserAuthorityPolicy authorityPolicy) {
        this.authorityPolicy = authorityPolicy;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }
}
