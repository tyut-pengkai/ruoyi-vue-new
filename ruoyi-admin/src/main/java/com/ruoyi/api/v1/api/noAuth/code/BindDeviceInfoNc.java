package com.ruoyi.api.v1.api.noAuth.code;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.*;
import com.ruoyi.api.v1.domain.vo.SysAppUserDeviceCodeVo;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysAppUserDeviceCode;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.service.ISysAppUserDeviceCodeService;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysDeviceCodeService;
import com.ruoyi.system.service.ISysLoginCodeService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

public class BindDeviceInfoNc extends Function {

    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private ISysLoginCodeService loginCodeService;
    @Resource
    private ISysDeviceCodeService deviceCodeService;
    @Resource
    private ISysAppUserDeviceCodeService appUserDeviceCodeService;

    @Override
    public void init() {
        this.setApi(new Api("bindDeviceInfo.nc", "获取用户绑定设备", false, Constants.API_TAG_CODE,
                "获取用户当前绑定的设备信息", new AuthType[]{AuthType.LOGIN_CODE}, Constants.BILL_TYPE_ALL, new Param[]{
                new Param("loginCode", true, "单码"),
        },
                new Resp(Resp.DataType.object, "设备信息列表，注意此处为数组", new RespItem[]{
                        new RespItem(Resp.DataType.string, "deviceCodeStr", "设备码"),
                        new RespItem(Resp.DataType.string, "lastLoginTime", "设备最后登录时间"),
                        new RespItem(Resp.DataType.string, "loginTimes", "设备登录次数"),
                        new RespItem(Resp.DataType.string, "status", "设备状态（0正常 1停用）"),
                        new RespItem(Resp.DataType.string, "remark", "备注信息"),
                })));
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
        List<SysAppUserDeviceCode> deviceCodeList = appUserDeviceCodeService.selectSysAppUserDeviceCodeByAppUserId(appUser.getAppUserId());
        return deviceCodeList.stream().map(item -> {
            item.setDeviceCode(deviceCodeService.selectSysDeviceCodeByDeviceCodeId(item.getDeviceCodeId()));
            return new SysAppUserDeviceCodeVo(item);
        }).collect(Collectors.toList());
    }
}
