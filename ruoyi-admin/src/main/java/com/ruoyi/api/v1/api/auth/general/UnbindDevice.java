package com.ruoyi.api.v1.api.auth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysAppUserDeviceCode;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.*;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.domain.SysAppUserExpireLog;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.domain.SysLoginCodeTemplate;
import com.ruoyi.system.domain.SysUnbindLog;
import com.ruoyi.system.service.ISysAppUserDeviceCodeService;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysCardTemplateService;
import com.ruoyi.system.service.ISysLoginCodeTemplateService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

public class UnbindDevice extends Function {

    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private ISysAppUserDeviceCodeService appUserDeviceCodeService;
    @Resource
    private ISysCardTemplateService cardTemplateService;
    @Resource
    private ISysLoginCodeTemplateService loginCodeTemplateService;

    @Override
    public void init() {
        this.setApi(new Api("unbindDevice.ag", "解除绑定当前设备", true, Constants.API_TAG_GENERAL,
                "解除绑定当前设备，解绑成功会根据软件设定扣减用户余额", Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL,
//                new Param[]{
//                        new Param("enableNegative", false, "是否允许用户过期(计时模式)或余额为负数(计点模式)，允许传1，不允许传0，默认为0")
//                },
                null,
                new Resp(Resp.DataType.string, "成功返回0，设备码不存在返回-1")));
    }

    @Override
    @Transactional(noRollbackFor = ApiException.class, rollbackFor = Exception.class)
    public Object handle() {
        // 是否开启解绑
        Boolean enableUnbind = Convert.toBool(this.getApp().getEnableUnbind(), false);
        if (!enableUnbind) {
            throw new ApiException(ErrorCode.ERROR_APP_UNBIND_NOT_ENABLE);
        }
        // 试用用户无法解绑
        LoginUser loginUser = getLoginUser();
        if (loginUser.getIfTrial()) {
            throw new ApiException(ErrorCode.ERROR_TRIAL_USER_NOT_ALLOWED);
        }
        // 用户
        SysAppUser appUser = appUserService.selectSysAppUserByAppUserId(loginUser.getAppUserId());
        // 卡类是否开启解绑
        if (appUser.getLastChargeTemplateId() != null) {
            if (this.getApp().getAuthType() == AuthType.ACCOUNT) {
                SysCardTemplate template = cardTemplateService.selectSysCardTemplateByTemplateId(appUser.getLastChargeTemplateId());
                if (template != null && UserConstants.NO.equals(template.getEnableUnbind())) {
                    throw new ApiException(ErrorCode.ERROR_CARD_UNBIND_NOT_ENABLE);
                }
            } else if (this.getApp().getAuthType() == AuthType.LOGIN_CODE) {
                SysLoginCodeTemplate template = loginCodeTemplateService.selectSysLoginCodeTemplateByTemplateId(appUser.getLastChargeTemplateId());
                if (template != null && UserConstants.NO.equals(template.getEnableUnbind())) {
                    throw new ApiException(ErrorCode.ERROR_CARD_UNBIND_NOT_ENABLE);
                }
            }
        }
        // 设备码是否存在
        SysAppUserDeviceCode appUserDeviceCode = appUserDeviceCodeService.selectSysAppUserDeviceCodeById(loginUser.getAppUserDeviceCode().getId());
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
        unbindLog.setDeviceCode(loginUser.getDeviceCode().getDeviceCode());
        unbindLog.setDeviceCodeId(appUserDeviceCode.getDeviceCodeId());
        unbindLog.setChangeAmount(0L);
        unbindLog.setExpireTimeAfter(null);
        unbindLog.setExpireTimeBefore(null);
        unbindLog.setPointAfter(null);
        unbindLog.setPointBefore(null);
        // 扣减解绑次数
        if (appUser.getUnbindTimes() > 0) {
            appUser.setUnbindTimes(appUser.getUnbindTimes() - 1);
            appUserService.updateSysAppUser(appUser);
        } else {
            if (Convert.toBool(this.getApp().getEnableUnbindByQuota(), true)) {
                Long p = this.getApp().getReduceQuotaUnbind();
                if (p != null && p > 0) {
//                  boolean enableNegative = Convert.toBool(this.getParams().get("enableNegative"), false);
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
                    AsyncManager.me().execute(AsyncFactory.recordDeviceUnbind(unbindLog));
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
