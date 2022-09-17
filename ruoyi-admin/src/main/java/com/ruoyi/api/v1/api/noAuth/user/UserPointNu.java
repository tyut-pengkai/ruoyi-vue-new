package com.ruoyi.api.v1.api.noAuth.user;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysUserService;

import javax.annotation.Resource;

public class UserPointNu extends Function {

    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private ISysUserService userService;

    @Override
    public void init() {
        this.setApi(new Api("userPoint.nu", "获取用户点数余额", false, Constants.API_TAG_POINT,
                "获取用户点数余额", new AuthType[]{AuthType.ACCOUNT}, new BillType[]{BillType.POINT},
                new Param[]{
                        new Param("username", true, "账号"),
                },
                new Resp(Resp.DataType.string, "用户用户点数余额")));
    }

    @Override
    public Object handle() {
        String username = this.getParams().get("username");
        SysUser user = userService.selectUserByUserName(username);
        if (user != null) {
            SysAppUser appUser = appUserService.selectSysAppUserByAppIdAndUserId(getApp().getAppId(), user.getUserId());
            if (appUser == null) {
                throw new ApiException(ErrorCode.ERROR_APP_USER_NOT_EXIST);
            }
            return appUser.getPoint();
        } else {
            throw new ApiException(ErrorCode.ERROR_ACCOUNT_NOT_EXIST);
        }
    }
}
