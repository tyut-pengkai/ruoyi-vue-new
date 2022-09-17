package com.ruoyi.api.v1.api.noAuth.code;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysLoginCodeService;

import javax.annotation.Resource;

public class UserPointNc extends Function {

    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private ISysLoginCodeService loginCodeService;

    @Override
    public void init() {
        this.setApi(new Api("userPoint.nc", "获取用户点数余额", false, Constants.API_TAG_POINT,
                "获取用户点数余额", new AuthType[]{AuthType.LOGIN_CODE}, new BillType[]{BillType.POINT},
                new Param[]{
                        new Param("loginCode", true, "单码"),
                },
                new Resp(Resp.DataType.string, "用户用户点数余额")));
    }

    @Override
    public Object handle() {
        String loginCodeStr = this.getParams().get("loginCode");
        SysLoginCode loginCode = loginCodeService.selectSysLoginCodeByCardNo(loginCodeStr);
        if (loginCode == null) {
            throw new ApiException(ErrorCode.ERROR_LOGIN_CODE_NOT_EXIST);
        }
        SysAppUser appUser = appUserService.selectSysAppUserByAppIdAndLoginCode(getApp().getAppId(), loginCodeStr);
        if (appUser == null) {
            throw new ApiException(ErrorCode.ERROR_APP_USER_NOT_EXIST);
        }
        return appUser.getPoint();
    }
}
