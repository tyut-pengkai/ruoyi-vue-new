package com.ruoyi.api.v1.api.auth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.common.core.domain.entity.SysAppTrialUser;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.service.ISysAppTrialUserService;
import com.ruoyi.system.service.ISysAppUserService;

import javax.annotation.Resource;

public class UserExpireTime extends Function {

    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private ISysAppTrialUserService appTrialUserService;

    @Override
    public void init() {
        this.setApi(new Api("userExpireTime.ag", "获取用户过期时间", true, Constants.API_TAG_TIME,
                "获取用户过期时间", Constants.AUTH_TYPE_ALL, new BillType[]{BillType.TIME}));
    }

    @Override
    public Object handle() {
        LoginUser loginUser = getLoginUser();
        if (loginUser.getIfTrial()) {
            SysAppTrialUser trialUser = appTrialUserService.selectSysAppTrialUserByAppTrialUserId(loginUser.getAppTrialUser().getAppTrialUserId());
            return DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, trialUser.getExpireTime());
        } else {
            SysAppUser appUser = appUserService.selectSysAppUserByAppUserId(loginUser.getAppUser().getAppUserId());
            return DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, appUser.getExpireTime());
        }
    }
}
