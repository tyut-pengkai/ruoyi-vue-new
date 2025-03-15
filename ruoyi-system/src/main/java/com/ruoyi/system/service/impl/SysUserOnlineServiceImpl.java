package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppVersion;
import com.ruoyi.common.core.domain.entity.SysDeviceCode;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysUserOnline;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysAppVersionService;
import com.ruoyi.system.service.ISysDeviceCodeService;
import com.ruoyi.system.service.ISysUserOnlineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 在线用户 服务层处理
 *
 * @author ruoyi
 */
@Service
public class SysUserOnlineServiceImpl implements ISysUserOnlineService {

    @Resource
    private ISysAppService appService;
    @Resource
    private ISysAppVersionService versionService;
    @Resource
    private ISysDeviceCodeService deviceCodeService;

    /**
     * 通过登录地址查询信息
     *
     * @param ipaddr 登录地址
     * @param user   用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginUser user)
    {
        if (StringUtils.equals(ipaddr, user.getIpaddr()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过用户名称查询信息
     *
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByUserName(String userName, LoginUser user)
    {
        if (StringUtils.equals(userName, user.getUsername()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过登录地址/用户名称查询信息
     *
     * @param ipaddr 登录地址
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginUser user)
    {
        if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 设置在线用户信息
     *
     * @param user 用户信息
     * @return 在线用户
     */
    @Override
    public SysUserOnline loginUserToUserOnline(LoginUser user)
    {
        if (StringUtils.isNull(user) || (!user.getIfApp() && StringUtils.isNull(user.getUser()))) {
            return null;
        }
        SysUserOnline sysUserOnline = new SysUserOnline();
        sysUserOnline.setTokenId(user.getToken());
        sysUserOnline.setUserName(user.getUsername());
        sysUserOnline.setIpaddr(user.getIpaddr());
        sysUserOnline.setLoginLocation(user.getLoginLocation());
        sysUserOnline.setBrowser(user.getBrowser());
        sysUserOnline.setOs(user.getOs());
        sysUserOnline.setLoginTime(user.getLoginTime());
        sysUserOnline.setExpireTime(user.getExpireTime());
        sysUserOnline.setIfApp(user.getIfApp() ? 'Y' : 'N');
        sysUserOnline.setIfTrial(user.getIfTrial() ? 'Y' : 'N');
        if (user.getIfApp()) {
            fillSysUserOnline(user, sysUserOnline);
            if (user.getDeviceCodeId() != null) {
                SysDeviceCode deviceCode = deviceCodeService.selectSysDeviceCodeByDeviceCodeId(user.getDeviceCodeId());
                sysUserOnline.setDeviceCode(deviceCode.getDeviceCode());
            }
        }
        if (user.getUser() != null && StringUtils.isNotNull(user.getUser().getDept())) {
            sysUserOnline.setDeptName(user.getUser().getDept().getDeptName());
        }

        return sysUserOnline;
    }

    public void fillSysUserOnline(LoginUser user, SysUserOnline sysUserOnline) {
        String appKey = user.getAppKey();
        if (appKey == null) {
            //noinspection deprecation
            appKey = user.getApp() != null ? user.getApp().getAppKey() : null;
        }
        SysAppVersion appVersion = versionService.selectSysAppVersionByAppVersionId(user.getAppVersionId());
        if (StringUtils.isNotBlank(appKey)) {
            SysApp app = appService.selectSysAppByAppKey(appKey);
//                System.out.println(JSON.toJSONString(user));
//                System.out.println(JSON.toJSONString(app));
            if(app != null && appVersion != null) {
                sysUserOnline.setAppDesc(app.getAppName() + "-" + appVersion.getVersionShow());
            } else {
                if(appVersion != null) {
                    sysUserOnline.setAppDesc("未知" + "-" + appVersion.getVersionShow());
                } else {
                    sysUserOnline.setAppDesc("未知");
                }
            }
            if(app != null) {
                sysUserOnline.setAppAuthor(app.getCreateBy());
            } else {
                sysUserOnline.setAppAuthor("未知");
            }
        } else {
            if(appVersion != null) {
                sysUserOnline.setAppDesc("未知" + "-" + appVersion.getVersionShow());
            } else {
                sysUserOnline.setAppDesc("未知");
            }
            sysUserOnline.setAppAuthor("未知");
        }
    }
}
