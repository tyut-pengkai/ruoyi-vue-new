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
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysUserService;

import javax.annotation.Resource;

public class UserExpireTimeNu extends Function {

    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private ISysUserService userService;

    @Override
    public void init() {
        this.setApi(new Api("userExpireTime.nu", "获取用户过期时间", false, Constants.API_TAG_ACCOUNT_TIME,
                "获取用户过期时间", new AuthType[]{AuthType.ACCOUNT}, new BillType[]{BillType.TIME},
                new Param[]{
                        new Param("username", true, "账号"),
                },
                new Resp(Resp.DataType.string, "用户过期时间")));
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
            return DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, appUser.getExpireTime());
        } else {
            throw new ApiException(ErrorCode.ERROR_ACCOUNT_NOT_EXIST);
        }
    }
}
