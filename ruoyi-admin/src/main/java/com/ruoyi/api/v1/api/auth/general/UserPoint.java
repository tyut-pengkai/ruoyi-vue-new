package com.ruoyi.api.v1.api.auth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.system.service.ISysAppUserService;

import javax.annotation.Resource;

public class UserPoint extends Function {

    @Resource
    private ISysAppUserService appUserService;

    @Override
    public void init() {
        this.setApi(new Api("userPoint.ag", "获取用户点数余额", true, Constants.API_TAG_POINT,
                "获取用户点数余额", Constants.AUTH_TYPE_ALL, new BillType[]{BillType.POINT}));
    }

    @Override
    public Object handle() {
        LoginUser loginUser = getLoginUser();
        if (loginUser.getIfTrial()) {
            return 0;
        }
        SysAppUser appUser = appUserService.selectSysAppUserByAppUserId(loginUser.getAppUser().getAppUserId());
        return appUser.getPoint();
    }
}
