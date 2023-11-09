package com.ruoyi.api.v1.api.noAuth.code;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysAppUserDeviceCode;
import com.ruoyi.common.core.domain.entity.SysDeviceCode;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.*;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.domain.SysAppUserExpireLog;
import com.ruoyi.system.domain.SysLoginCodeTemplate;
import com.ruoyi.system.domain.SysUnbindLog;
import com.ruoyi.system.service.*;
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
    @Resource
    private ISysLoginCodeTemplateService loginCodeTemplateService;

    @Override
    public void init() {
        this.setApi(new Api("unbindDevice.nc", "解除绑定指定设备", false, Constants.API_TAG_CODE,
                "解除绑定当前设备，解绑成功会根据软件设定扣减用户余额", new AuthType[]{AuthType.LOGIN_CODE}, Constants.BILL_TYPE_ALL,
                new Param[]{
                        new Param("deviceCode", true, "设备码"),
                        new Param("loginCode", true, "单码"),
//                        new Param("enableNegative", false, "是否允许用户过期(计时模式)或余额为负数(计点模式)，允许传1，不允许传0，默认为0")
                }, new Resp(Resp.DataType.string, "成功返回0，设备码不存在返回-1")));
    }

    @Override
    @Transactional(noRollbackFor = ApiException.class, rollbackFor = Exception.class)
    public Object handle() {
        // 软件是否开启解绑
        Boolean enableUnbind = Convert.toBool(this.getApp().getEnableUnbind(), false);
        if (!enableUnbind) {
            throw new ApiException(ErrorCode.ERROR_APP_UNBIND_NOT_ENABLE);
        }
        // 验证单码
        String loginCodeStr = this.getParams().get("loginCode");
//        SysLoginCode loginCode = loginCodeService.selectSysLoginCodeByCardNo(loginCodeStr);
//        if (loginCode == null) {
//            throw new ApiException(ErrorCode.ERROR_LOGIN_CODE_NOT_EXIST);
//        }
        // 用户是否存在
        SysAppUser appUser = appUserService.selectSysAppUserByAppIdAndLoginCode(getApp().getAppId(), loginCodeStr);
        if (appUser == null) {
            throw new ApiException(ErrorCode.ERROR_APP_USER_NOT_EXIST);
        }
        // 卡类是否开启解绑
        if (appUser.getLastChargeTemplateId() != null) {
            SysLoginCodeTemplate template = loginCodeTemplateService.selectSysLoginCodeTemplateByTemplateId(appUser.getLastChargeTemplateId());
            if (template != null && UserConstants.NO.equals(template.getEnableUnbind())) {
                throw new ApiException(ErrorCode.ERROR_CARD_UNBIND_NOT_ENABLE);
            }
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
        // 解绑日志
        SysUnbindLog unbindLog = new SysUnbindLog();
        unbindLog.setAppUserId(appUser.getAppUserId());
        unbindLog.setAppId(this.getApp().getAppId());
        unbindLog.setFirstLoginTime(appUserDeviceCode.getCreateTime());
        unbindLog.setLastLoginTime(appUserDeviceCode.getLastLoginTime());
        unbindLog.setLoginTimes(appUserDeviceCode.getLoginTimes());
        unbindLog.setUnbindType(UnbindType.CALL_API_UNBIND);
        unbindLog.setUnbindDesc("API：" + this.getApi().getApi());
        unbindLog.setDeviceCode(deviceCodeStr);
        unbindLog.setDeviceCodeId(appUserDeviceCode.getDeviceCodeId());
        unbindLog.setChangeAmount(0L);
        if (this.getApp().getBillType() == BillType.TIME) {
            unbindLog.setExpireTimeAfter(appUser.getExpireTime());
            unbindLog.setExpireTimeBefore(appUser.getExpireTime());
        }
        if (this.getApp().getBillType() == BillType.POINT) {
            unbindLog.setPointAfter(appUser.getPoint());
            unbindLog.setPointBefore(appUser.getPoint());
        }
        // 扣减解绑次数
        if (appUser.getUnbindTimes() > 0) {
            appUser.setUnbindTimes(appUser.getUnbindTimes() - 1);
            appUserService.updateSysAppUser(appUser);
        } else {
            if (Convert.toBool(this.getApp().getEnableUnbindByQuota(), true)) {
                Long p = this.getApp().getReduceQuotaUnbind();
                if (p != null && p > 0) {
//                   boolean enableNegative = Convert.toBool(this.getParams().get("enableNegative"), false);
                    boolean enableNegative = Convert.toBool(this.getApp().getEnableNegative(), false);
                    SysAppUserExpireLog expireLog = new SysAppUserExpireLog();
                    if (this.getApp().getBillType() == BillType.TIME) {
                        expireLog.setExpireTimeBefore(appUser.getExpireTime());
                        Date newExpiredTime = MyUtils.getNewExpiredTimeSub(appUser.getExpireTime(), p);
                        Date nowDate = DateUtils.getNowDate();
                        if ((appUser.getExpireTime().after(nowDate) && newExpiredTime.after(nowDate)) || enableNegative) {
                            appUser.setExpireTime(newExpiredTime);
                            appUserService.updateSysAppUser(appUser);
                            expireLog.setExpireTimeAfter(newExpiredTime);
                        } else {
                            throw new ApiException(ErrorCode.ERROR_APP_USER_NO_TIME);
                        }
                    } else if (this.getApp().getBillType() == BillType.POINT) {
                        expireLog.setPointBefore(appUser.getPoint());
                        BigDecimal point = BigDecimal.valueOf(p);
                        BigDecimal newPoint = appUser.getPoint().subtract(point);
                        if (appUser.getPoint().compareTo(point) >= 0 || enableNegative) {
                            appUser.setPoint(newPoint);
                            appUserService.updateSysAppUser(appUser);
                            expireLog.setPointAfter(newPoint);
                        } else {
                            throw new ApiException(ErrorCode.ERROR_APP_USER_NO_POINT);
                        }
                    }
                    // 记录用户时长变更日志
                    expireLog.setAppUserId(appUser.getAppUserId());
                    expireLog.setTemplateId(null);
                    expireLog.setCardId(null);
                    expireLog.setChangeDesc("API：" + this.getApi().getApi());
                    expireLog.setChangeType(AppUserExpireChangeType.UNBIND);
                    expireLog.setChangeAmount(-p);
                    expireLog.setCardNo(null);
                    expireLog.setAppId(this.getApp().getAppId());
                    AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog));
                    // 记录解绑日志
                    unbindLog.setChangeAmount(-p);
                    unbindLog.setExpireTimeAfter(expireLog.getExpireTimeAfter());
                    unbindLog.setExpireTimeBefore(expireLog.getExpireTimeBefore());
                    unbindLog.setPointAfter(expireLog.getPointAfter());
                    unbindLog.setPointBefore(expireLog.getPointBefore());
                }
            } else {
                throw new ApiException(ErrorCode.ERROR_UNBIND_NO_TIMES);
            }
        }
        // 解绑
        appUserDeviceCodeService.deleteSysAppUserDeviceCodeById(appUserDeviceCode.getId());
        AsyncManager.me().execute(AsyncFactory.recordDeviceUnbind(unbindLog));
        return 0;
    }
}
