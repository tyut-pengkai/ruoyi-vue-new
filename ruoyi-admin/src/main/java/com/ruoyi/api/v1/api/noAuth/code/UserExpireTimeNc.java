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
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysLoginCodeService;

import javax.annotation.Resource;

public class UserExpireTimeNc extends Function {

    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private ISysLoginCodeService loginCodeService;

    @Override
    public void init() {
        this.setApi(new Api("userExpireTime.nc", "获取用户过期时间", false, Constants.API_TAG_TIME,
                "获取用户过期时间", new AuthType[]{AuthType.LOGIN_CODE}, new BillType[]{BillType.TIME},
                new Param[]{
                        new Param("loginCode", true, "单码"),
                },
                new Resp(Resp.DataType.string, "用户过期时间")));
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
        return DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, appUser.getExpireTime());
    }
}
