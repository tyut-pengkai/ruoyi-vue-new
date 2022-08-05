package com.ruoyi.api.v1.api.auth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.service.ISysAppUserService;

import javax.annotation.Resource;
import java.util.Date;

public class ReduceTime extends Function {

    @Resource
    private ISysAppUserService appUserService;

    @Override
    public void init() {
        this.setApi(new Api("reduceTime.ag", "扣减用户时间", true, Constants.API_TAG_TIME,
                "扣减用户时间，返回扣减后到期时间", Constants.AUTH_TYPE_ALL, new BillType[]{BillType.TIME},
                new Param[]{
                        new Param("seconds", true, "扣减的秒数，需传入正整数"),
                        new Param("enableNegative", false, "是否允许用户过期，允许传1，不允许传0，默认为0")
                }
        ));
    }

    @Override
    public Object handle() {
        LoginUser loginUser = getLoginUser();
        if (loginUser.getIfTrial()) {
            throw new ApiException(ErrorCode.ERROR_TRIAL_USER_NOT_ALLOWED);
        }
        String s = this.getParams().get("seconds");
        boolean enableNegative = Convert.toBool(this.getParams().get("enableNegative"), false);
        Long seconds = null;
        try {
            seconds = Long.parseLong(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new ApiException(ErrorCode.ERROR_PARAMETERS_ERROR, "seconds必须为正整数");
        }
        if (seconds <= 0) {
            throw new ApiException(ErrorCode.ERROR_PARAMETERS_ERROR, "seconds必须大于0");
        }
        SysAppUser appUser = appUserService.selectSysAppUserByAppUserId(loginUser.getAppUser().getAppUserId());
        Date newExpiredTime = MyUtils.getNewExpiredTimeSub(appUser.getExpireTime(), seconds);
        Date nowDate = DateUtils.getNowDate();
        if ((appUser.getExpireTime().after(nowDate) && newExpiredTime.after(nowDate)) || enableNegative) {
            appUser.setExpireTime(newExpiredTime);
            appUserService.updateSysAppUser(appUser);
        } else {
            throw new ApiException(ErrorCode.ERROR_APP_USER_NO_TIME);
        }
        return DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, appUser.getExpireTime());
    }
}
