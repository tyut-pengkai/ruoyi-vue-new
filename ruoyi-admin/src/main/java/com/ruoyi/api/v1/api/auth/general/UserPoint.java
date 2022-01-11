package com.ruoyi.api.v1.api.auth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.system.service.ISysAppUserService;

import javax.annotation.Resource;

public class UserPoint extends Function {

    @Resource
    private ISysAppUserService appUserService;

    @Override
    public void init() {
        this.setApi(new Api("userPoint.ag", "获取用户点数余额", true, Constants.API_TAG_TIME, "获取用户点数余额"));
    }

    @Override
    public Object handle() {
        SysAppUser appUser = appUserService.selectSysAppUserByAppUserId(getLoginUser().getAppUser().getAppUserId());
        return appUser.getPoint();
    }
}
