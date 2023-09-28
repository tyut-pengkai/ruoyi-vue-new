package com.ruoyi.api.v1.api.noAuth.user;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.*;
import com.ruoyi.api.v1.domain.vo.SysAppUserVo;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysUserService;

import javax.annotation.Resource;

public class AppUserInfoNu extends Function {

    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private ISysUserService userService;

    @Override
    public void init() {
        this.setApi(new Api("appUserInfo.nu", "获取用户信息", false, Constants.API_TAG_ACCOUNT,
                "获取用户信息", new AuthType[]{AuthType.ACCOUNT}, Constants.BILL_TYPE_ALL, new Param[]{
                new Param("username", true, "账号")
        },
                new Resp(Resp.DataType.object, null,
                        new RespItem[]{
                                new RespItem(Resp.DataType.string, "expireTime", "用户过期时间，计时模式下有效"),
                                new RespItem(Resp.DataType.integer, "point", "用户剩余点数，计点模式下有效"),
                                new RespItem(Resp.DataType.string, "status", "用户状态，0为正常"),
                                new RespItem(Resp.DataType.integer, "loginLimitU", "用户配置的同时在线用户数限制"),
                                new RespItem(Resp.DataType.integer, "loginLimitM", "用户配置的同时在线设备数限制"),
                                new RespItem(Resp.DataType.integer, "appLoginLimitU", "软件配置的同时在线用户数限制"),
                                new RespItem(Resp.DataType.integer, "appLoginLimitM", "软件配置的同时在线设备数限制"),
                                new RespItem(Resp.DataType.integer, "cardLoginLimitU", "卡密配置的同时在线用户数限制"),
                                new RespItem(Resp.DataType.integer, "cardLoginLimitM", "卡密配置的同时在线设备数限制"),
                                new RespItem(Resp.DataType.integer, "effectiveLoginLimitU", "最终生效的同时在线用户数限制"),
                                new RespItem(Resp.DataType.integer, "effectiveLoginLimitM", "最终生效的同时在线设备数限制"),
                                new RespItem(Resp.DataType.integer, "currentOnlineU", "当前在线用户数"),
                                new RespItem(Resp.DataType.integer, "currentOnlineM", "当前在线设备数"),
                                new RespItem(Resp.DataType.integer, "loginTimes", "用户登录次数"),
                                new RespItem(Resp.DataType.string, "lastLoginTime", "最近登录时间"),
                                new RespItem(Resp.DataType.string, "loginIp", "最近登录IP"),
                                new RespItem(Resp.DataType.number, "freeBalance", "当前无实际作用"),
                                new RespItem(Resp.DataType.number, "payBalance", "当前无实际作用"),
                                new RespItem(Resp.DataType.number, "freePayment", "当前无实际作用"),
                                new RespItem(Resp.DataType.number, "payPayment", "当前无实际作用"),
                                new RespItem(Resp.DataType.string, "cardCustomParams", "由卡密继承来的自定义参数"),
                                new RespItem(Resp.DataType.string, "remark", "备注信息"),
                                new RespItem(Resp.DataType.object, "userInfo", "账号信息，账号模式下有效",
                                        new RespItem[]{
                                                new RespItem(Resp.DataType.string, "avatar", "头像"),
                                                new RespItem(Resp.DataType.string, "userName", "用户名"),
                                                new RespItem(Resp.DataType.string, "nickName", "用户昵称"),
                                                new RespItem(Resp.DataType.string, "sex", "性别，0男1女2未知"),
                                                new RespItem(Resp.DataType.string, "phonenumber", "手机号码"),
                                                new RespItem(Resp.DataType.string, "email", "邮箱"),
                                                new RespItem(Resp.DataType.bool, "admin", "是否为管理员账号"),
                                                new RespItem(Resp.DataType.number, "availablePayBalance", "可用余额"),
                                                new RespItem(Resp.DataType.number, "freezePayBalance", "冻结余额"),
                                                new RespItem(Resp.DataType.string, "loginDate", "最近登录时间"),
                                                new RespItem(Resp.DataType.string, "loginIp", "最近登录IP"),
                                        }),
//                                new RespItem(Resp.DataType.string, "loginCode", "单码信息，单码模式下有效"),
                        })));
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
            appUser.setUser(userService.selectUserById(appUser.getUserId()));
            appUserService.fillCurrentOnlineInfo(appUser);
            return new SysAppUserVo(appUser);
        } else {
            throw new ApiException(ErrorCode.ERROR_ACCOUNT_NOT_EXIST);
        }
    }
}
