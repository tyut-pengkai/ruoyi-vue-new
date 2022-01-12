package com.ruoyi.api.v1.api.auth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.vo.SysAppUserVo;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.system.service.ISysAppUserService;

import javax.annotation.Resource;

public class AppUserInfo extends Function {

    @Resource
    private ISysAppUserService appUserService;

    @Override
    public void init() {
        this.setApi(new Api("appUserInfo.ag", "获取用户信息", true, Constants.API_TAG_GENERAL,
                "获取用户信息", Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL));
    }

    @Override
    public Object handle() {
        SysAppUser appUser = appUserService.selectSysAppUserByAppUserId(getLoginUser().getAppUser().getAppUserId());
        return new SysAppUserVo(appUser);
    }
}
