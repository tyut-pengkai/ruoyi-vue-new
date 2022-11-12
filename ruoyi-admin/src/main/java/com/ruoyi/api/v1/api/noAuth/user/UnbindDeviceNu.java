package com.ruoyi.api.v1.api.noAuth.user;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysAppUserDeviceCode;
import com.ruoyi.common.core.domain.entity.SysDeviceCode;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.service.ISysAppUserDeviceCodeService;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysDeviceCodeService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

public class UnbindDeviceNu extends Function {

    @Resource
    private ISysUserService userService;
    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private ISysDeviceCodeService deviceCodeService;
    @Resource
    private ISysAppUserDeviceCodeService appUserDeviceCodeService;

    @Override
    public void init() {
        this.setApi(new Api("unbindDevice.nu", "解除绑定指定设备", false, Constants.API_TAG_ACCOUNT,
                "解除绑定当前设备，解绑成功会根据软件设定扣减用户余额", new AuthType[]{AuthType.ACCOUNT}, Constants.BILL_TYPE_ALL,
                new Param[]{
                        new Param("deviceCode", true, "设备码"),
                        new Param("username", true, "账号"),
                        new Param("password", true, "密码"),
//                        new Param("enableNegative", false, "是否允许用户过期(计时模式)或余额为负数(计点模式)，允许传1，不允许传0，默认为0")
                }, new Resp(Resp.DataType.string, "成功返回0，设备码不存在返回-1")));
    }

    @Override
    @Transactional(noRollbackFor = ApiException.class, rollbackFor = Exception.class)
    public Object handle() {
        // 是否开启解绑
        Boolean enableUnbind = Convert.toBool(this.getApp().getEnableUnbind(), false);
        if (!enableUnbind) {
            throw new ApiException(ErrorCode.ERROR_UNBIND_NOT_ENABLE);
        }
        // 验证账号密码
        String username = this.getParams().get("username");
        String password = this.getParams().get("password");
        SysUser user = userService.selectUserByUserName(username);
        if (user == null) {
            throw new ApiException(ErrorCode.ERROR_ACCOUNT_NOT_EXIST);
        }
        if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
            throw new ApiException(ErrorCode.ERROR_USERNAME_OR_PASSWORD_ERROR, "密码有误");
        }
        // 用户是否存在
        SysAppUser appUser = appUserService.selectSysAppUserByAppIdAndUserId(getApp().getAppId(), user.getUserId());
        if (appUser == null) {
            throw new ApiException(ErrorCode.ERROR_APP_USER_NOT_EXIST);
        }
        // 设备码是否存在
        String deviceCodeStr = this.getParams().get("deviceCode");
        SysDeviceCode deviceCode = deviceCodeService.selectSysDeviceCodeByDeviceCode(deviceCodeStr);
        if (deviceCode == null) {
            return "-1";
        }
        SysAppUserDeviceCode appUserDeviceCode = appUserDeviceCodeService.selectSysAppUserDeviceCodeByAppUserIdAndDeviceCodeId(appUser.getAppUserId(), deviceCode.getDeviceCodeId());
        if (appUserDeviceCode == null) {
            return "-1";
        }
        // 扣减解绑次数
        if (appUser.getUnbindTimes() > 0) {
            appUser.setUnbindTimes(appUser.getUnbindTimes() - 1);
            appUserService.updateSysAppUser(appUser);
        } else {
            if (Convert.toBool(this.getApp().getEnableUnbindByQuota(), true)) {
                Long p = this.getApp().getReduceQuotaUnbind();
                if (p != null || p > 0) {
//                  boolean enableNegative = Convert.toBool(this.getParams().get("enableNegative"), false);
                    boolean enableNegative = Convert.toBool(this.getApp().getEnableNegative(), false);
                    if (this.getApp().getBillType() == BillType.TIME) {
                        Date newExpiredTime = MyUtils.getNewExpiredTimeSub(appUser.getExpireTime(), p);
                        Date nowDate = DateUtils.getNowDate();
                        if ((appUser.getExpireTime().after(nowDate) && newExpiredTime.after(nowDate)) || enableNegative) {
                            appUser.setExpireTime(newExpiredTime);
                            appUserService.updateSysAppUser(appUser);
                        } else {
                            throw new ApiException(ErrorCode.ERROR_APP_USER_NO_TIME);
                        }
                    } else if (this.getApp().getBillType() == BillType.POINT) {
                        BigDecimal point = BigDecimal.valueOf(p);
                        if (appUser.getPoint().compareTo(point) >= 0 || enableNegative) {
                            appUser.setPoint(appUser.getPoint().subtract(point));
                            appUserService.updateSysAppUser(appUser);
                        } else {
                            throw new ApiException(ErrorCode.ERROR_APP_USER_NO_POINT);
                        }
                    }
                }
            } else {
                throw new ApiException(ErrorCode.ERROR_UNBIND_NO_TIMES);
            }
        }
        // 解绑
        appUserDeviceCodeService.deleteSysAppUserDeviceCodeById(appUserDeviceCode.getId());
        return "0";
    }
}
