package com.ruoyi.common.core.domain.model;

import com.ruoyi.common.core.domain.entity.SysUser;

/**
 * 微信登录响应对象
 *
 * @author suguan
 * @date 2024-12-19 10:00:00
 */
public class WxLoginResponse
{
    /**
     * 登录状态：0-需要获取手机号，1-登录成功，2-需要完善信息
     */
    private Integer loginStatus;
    
    /**
     * 登录令牌（仅在登录成功时返回）
     */
    private String token;
    
    /**
     * 用户openId
     */
    private String openId;
    
    /**
     * 是否已绑定手机号
     */
    private Boolean hasPhone;
    
    /**
     * 用户信息（仅在需要完善信息时返回）
     */
    private SysUser userInfo;
    
    /**
     * 提示信息
     */
    private String message;

    public WxLoginResponse() {
    }

    public WxLoginResponse(Integer loginStatus, String message) {
        this.loginStatus = loginStatus;
        this.message = message;
    }

    public Integer getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Boolean getHasPhone() {
        return hasPhone;
    }

    public void setHasPhone(Boolean hasPhone) {
        this.hasPhone = hasPhone;
    }

    public SysUser getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(SysUser userInfo) {
        this.userInfo = userInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
} 