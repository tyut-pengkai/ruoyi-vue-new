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
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.domain.SysLoginCodeTemplate;
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
        SysAppUser appUser = appUserService.selectSysAppUserByAppUserId(loginUser.getAppUser().getAppUserId());
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
        SysAppUserDeviceCode appUserDeviceCode = loginUser.getAppUserDeviceCode();
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
                if (p != null && p > 0) {
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
