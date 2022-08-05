package com.ruoyi.api.v1.api.auth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.vo.SysAppTrialUserVo;
import com.ruoyi.api.v1.domain.vo.SysAppUserVo;
import com.ruoyi.common.core.domain.entity.SysAppTrialUser;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.system.service.ISysAppTrialUserService;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysUserService;

import javax.annotation.Resource;

public class AppUserInfo extends Function {

    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private ISysAppTrialUserService appTrialUserService;
    @Resource
    private ISysUserService userService;

    @Override
    public void init() {
        this.setApi(new Api("appUserInfo.ag", "获取用户信息", true, Constants.API_TAG_GENERAL,
                "获取用户信息", Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL));
    }

    @Override
    public Object handle() {
        LoginUser loginUser = getLoginUser();
        if (loginUser.getIfTrial()) {
            SysAppTrialUser appUser = appTrialUserService.selectSysAppTrialUserByAppTrialUserId(loginUser.getAppTrialUser().getAppTrialUserId());
            return new SysAppTrialUserVo(appUser);
        } else {
            SysAppUser appUser = appUserService.selectSysAppUserByAppUserId(loginUser.getAppUser().getAppUserId());
            appUser.setUser(userService.selectUserById(appUser.getUserId()));
            return new SysAppUserVo(appUser);
        }
    }
}
