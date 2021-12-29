package com.ruoyi.api.v1.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SysApp;
import com.ruoyi.system.domain.SysAppUser;
import com.ruoyi.system.domain.SysAppVersion;
import com.ruoyi.system.domain.SysDeviceCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class AppLoginUser implements UserDetails {
    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 用户信息
     */
    private SysUser user;

    private SysApp app;

    private SysAppUser appUser;

    private SysAppVersion appVersion;

    private SysDeviceCode deviceCode;

    public AppLoginUser() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JSONField(serialize = false)
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    /**
     * 账户是否未过期,过期无法验证
     */
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 指定用户是否解锁,锁定的用户无法进行身份验证
     *
     * @return
     */
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     *
     * @return
     */
    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用 ,禁用的用户不能身份验证
     *
     * @return
     */
    @JSONField(serialize = false)
    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getLoginLocation() {
        return loginLocation;
    }

    public void setLoginLocation(String loginLocation) {
        this.loginLocation = loginLocation;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public SysApp getApp() {
        return app;
    }

    public void setApp(SysApp app) {
        this.app = app;
    }

    public SysAppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(SysAppUser appUser) {
        this.appUser = appUser;
    }

    public SysAppVersion getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(SysAppVersion appVersion) {
        this.appVersion = appVersion;
    }

    public SysDeviceCode getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(SysDeviceCode deviceCode) {
        this.deviceCode = deviceCode;
    }
}
