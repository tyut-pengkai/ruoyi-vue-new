package com.ruoyi.common.core.domain.model;

import com.alibaba.fastjson2.annotation.JSONField;
import com.ruoyi.common.core.domain.entity.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * 登录用户身份权限
 * 
 * @author ruoyi
 */
public class LoginUser implements UserDetails
{
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

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
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 权限列表
     */
    private Set<String> permissions;

    private Boolean ifApp;

    private SysApp app;

    private SysAppUser appUser;

    private SysAppVersion appVersion;

    private SysDeviceCode deviceCode;

    private SysAppUserDeviceCode appUserDeviceCode;

    /**
     * 用户信息
     */
    private SysUser user;

    private SysAppTrialUser appTrialUser;

    private Boolean ifTrial = false;

    public SysAppTrialUser getAppTrialUser() {
        return appTrialUser;
    }

    public void setAppTrialUser(SysAppTrialUser appTrialUser) {
        this.appTrialUser = appTrialUser;
    }

    public Boolean getIfTrial() {
        return ifTrial;
    }

    public void setIfTrial(Boolean ifTrial) {
        this.ifTrial = ifTrial;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public LoginUser()
    {
    }

    public LoginUser(SysUser user, Set<String> permissions)
    {
        this.user = user;
        this.permissions = permissions;
    }

    public LoginUser(Long userId, Long deptId, SysUser user, Set<String> permissions)
    {
        this.userId = userId;
        this.deptId = deptId;
        this.user = user;
        this.permissions = permissions;
    }

    @JSONField(serialize = false)
    @Override
    public String getPassword()
    {
        return user.getPassword();
    }

    @Override
    public String getUsername()
    {
        return user != null ? user.getUserName() : (appUser != null ? appUser.getUserName() : appTrialUser != null ? appTrialUser.getUserName() : null);
    }

    /**
     * 账户是否未过期,过期无法验证
     */
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    /**
     * 指定用户是否解锁,锁定的用户无法进行身份验证
     * 
     * @return
     */
    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     * 
     * @return
     */
    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    /**
     * 是否可用 ,禁用的用户不能身份验证
     * 
     * @return
     */
    @JSONField(serialize = false)
    @Override
    public boolean isEnabled()
    {
        return true;
    }

    public Long getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(Long loginTime)
    {
        this.loginTime = loginTime;
    }

    public String getIpaddr()
    {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr)
    {
        this.ipaddr = ipaddr;
    }

    public String getLoginLocation()
    {
        return loginLocation;
    }

    public void setLoginLocation(String loginLocation)
    {
        this.loginLocation = loginLocation;
    }

    public String getBrowser()
    {
        return browser;
    }

    public void setBrowser(String browser)
    {
        this.browser = browser;
    }

    public String getOs()
    {
        return os;
    }

    public void setOs(String os)
    {
        this.os = os;
    }

    public Long getExpireTime()
    {
        return expireTime;
    }

    public void setExpireTime(Long expireTime)
    {
        this.expireTime = expireTime;
    }

    public Set<String> getPermissions()
    {
        return permissions;
    }

    public void setPermissions(Set<String> permissions)
    {
        this.permissions = permissions;
    }

    public SysUser getUser()
    {
        return user;
    }

    public void setUser(SysUser user)
    {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
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

    public Boolean getIfApp() {
        return ifApp;
    }

    public void setIfApp(Boolean ifApp) {
        this.ifApp = ifApp;
    }

    public SysAppUserDeviceCode getAppUserDeviceCode() {
        return appUserDeviceCode;
    }

    public void setAppUserDeviceCode(SysAppUserDeviceCode appUserDeviceCode) {
        this.appUserDeviceCode = appUserDeviceCode;
    }
}
