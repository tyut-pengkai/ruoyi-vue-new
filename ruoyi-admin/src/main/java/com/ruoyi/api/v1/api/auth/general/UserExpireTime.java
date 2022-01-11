package com.ruoyi.api.v1.api.auth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.service.ISysAppUserService;

import javax.annotation.Resource;

public class UserExpireTime extends Function {

    @Resource
    private ISysAppUserService appUserService;

    @Override
    public void init() {
        this.setApi(new Api("userExpireTime.ag", "获取用户过期时间", true, Constants.API_TAG_TIME, "获取用户过期时间"));
    }

    @Override
    public Object handle() {
        SysAppUser appUser = appUserService.selectSysAppUserByAppUserId(getLoginUser().getAppUser().getAppUserId());
        return DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, appUser.getExpireTime());
    }
}
