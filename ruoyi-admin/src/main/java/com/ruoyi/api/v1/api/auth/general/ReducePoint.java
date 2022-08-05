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
import com.ruoyi.system.service.ISysAppUserService;

import javax.annotation.Resource;
import java.math.BigDecimal;

public class ReducePoint extends Function {

    @Resource
    private ISysAppUserService appUserService;

    @Override
    public void init() {
        this.setApi(new Api("reducePoint.ag", "扣减用户点数", true, Constants.API_TAG_POINT,
                "扣减用户点数，返回扣减后点数余额", Constants.AUTH_TYPE_ALL, new BillType[]{BillType.POINT},
                new Param[]{
                        new Param("point", true, "扣减的点数，需传入正数，可精确到两位小数"),
                        new Param("enableNegative", false, "是否允许余额为负数，允许传1，不允许传0，默认为0")
                }
        ));
    }

    @Override
    public Object handle() {
        LoginUser loginUser = getLoginUser();
        if (loginUser.getIfTrial()) {
            throw new ApiException(ErrorCode.ERROR_TRIAL_USER_NOT_ALLOWED);
        }
        String p = this.getParams().get("point");
        boolean enableNegative = Convert.toBool(this.getParams().get("enableNegative"), false);
        BigDecimal point = null;
        try {
            point = BigDecimal.valueOf(Double.parseDouble(p));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new ApiException(ErrorCode.ERROR_PARAMETERS_ERROR, "point必须为数字");
        }
        if (point.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException(ErrorCode.ERROR_PARAMETERS_ERROR, "point必须大于0");
        }
        SysAppUser appUser = appUserService.selectSysAppUserByAppUserId(loginUser.getAppUser().getAppUserId());
        if (appUser.getPoint().compareTo(point) >= 0 || enableNegative) {
            appUser.setPoint(MyUtils.getNewPointSub(appUser.getPoint(), point.doubleValue()));
            appUserService.updateSysAppUser(appUser);
        } else {
            throw new ApiException(ErrorCode.ERROR_APP_USER_NO_POINT);
        }
        return appUser.getPoint();
    }
}
