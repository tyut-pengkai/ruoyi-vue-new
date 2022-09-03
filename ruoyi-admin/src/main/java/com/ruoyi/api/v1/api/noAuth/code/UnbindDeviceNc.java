package com.ruoyi.api.v1.api.noAuth.code;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysAppUserDeviceCode;
import com.ruoyi.common.core.domain.entity.SysDeviceCode;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.service.ISysAppUserDeviceCodeService;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysDeviceCodeService;
import com.ruoyi.system.service.ISysLoginCodeService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

public class UnbindDeviceNc extends Function {

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
        this.setApi(new Api("unbindDevice.nc", "解除绑定指定设备", false, Constants.API_TAG_CODE,
                "解除绑定当前设备，解绑成功会根据软件设定扣减用户余额", new AuthType[]{AuthType.LOGIN_CODE}, Constants.BILL_TYPE_ALL,
                new Param[]{
                        new Param("deviceCode", true, "设备码"),
                        new Param("loginCode", true, "单码"),
                        new Param("enableNegative", false, "是否允许用户过期(计时模式)或余额为负数(计点模式)，允许传1，不允许传0，默认为0")
                }, new Resp(Resp.DataType.string, "成功返回0，设备码不存在返回-1")));
    }

    @Override
    @Transactional(noRollbackFor = ApiException.class, rollbackFor = Exception.class)
    public Object handle() {
        String deviceCodeStr = this.getParams().get("deviceCode");
        String loginCodeStr = this.getParams().get("loginCode");

        SysLoginCode loginCode = loginCodeService.selectSysLoginCodeByCardNo(loginCodeStr);
        if (loginCode == null) {
            throw new ApiException(ErrorCode.ERROR_LOGIN_CODE_NOT_EXIST);
        }
        SysAppUser appUser = appUserService.selectSysAppUserByAppIdAndLoginCode(getApp().getAppId(), loginCodeStr);
        if (appUser == null) {
            throw new ApiException(ErrorCode.ERROR_APP_USER_NOT_EXIST);
        }
        // 解绑
        SysDeviceCode deviceCode = deviceCodeService.selectSysDeviceCodeByDeviceCode(deviceCodeStr);
        if (deviceCode == null) {
            throw new ApiException(ErrorCode.ERROR_DEVICE_CODE_NOT_EXIST);
        }
        SysAppUserDeviceCode appUserDeviceCode = appUserDeviceCodeService.selectSysAppUserDeviceCodeByAppUserIdAndDeviceCodeId(appUser.getAppUserId(), deviceCode.getDeviceCodeId());
        if (appUserDeviceCode == null) {
            return "-1";
        }
        appUserDeviceCodeService.deleteSysAppUserDeviceCodeById(appUserDeviceCode.getId());
        // 扣减
        Long p = this.getApp().getReduceQuotaUnbind();
        if (p == null || p <= 0) {
            return "0";
        }
        boolean enableNegative = Convert.toBool(this.getParams().get("enableNegative"), false);

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
        return "0";
    }
}
