package com.ruoyi.api.v1.api.auth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.api.v1.utils.ValidUtils;
import com.ruoyi.common.core.domain.entity.SysAppTrialUser;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.service.ISysAppTrialUserService;
import com.ruoyi.system.service.ISysAppUserService;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 当软件开启按时间段试用且试用用户在有效试用时间段内时，取过期时间API和取剩余点数API将分别返回试用结束时间和1，否则将分别返回实际过期时间和0
 */
public class UserPoint extends Function {

    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private ISysAppTrialUserService appTrialUserService;
    @Resource
    private ValidUtils validUtils;

    @Override
    public void init() {
        this.setApi(new Api("userPoint.ag", "获取用户点数余额", true, Constants.API_TAG_POINT,
                "获取用户点数余额", Constants.AUTH_TYPE_ALL, new BillType[]{BillType.POINT}, null,
                new Resp(Resp.DataType.string, "用户用户点数余额")));
    }

    @Override
    public Object handle() {
        LoginUser loginUser = getLoginUser();
        if (loginUser.getIfTrial()) {
            SysAppTrialUser trialUser = appTrialUserService.selectSysAppTrialUserByAppTrialUserId(loginUser.getAppTrialUser().getAppTrialUserId());
            if (validUtils.checkInTrialQuantum(getApp())) {
                Date trialQuantumEndTime = validUtils.getTrialQuantumEndTime(getApp());
                if (trialQuantumEndTime != null && trialQuantumEndTime.after(trialUser.getExpireTime())) {
                    return trialQuantumEndTime.before(DateUtils.getNowDate()) ? 0 : 1;
                }
            }
            return trialUser.getExpireTime().before(DateUtils.getNowDate()) ? 0 : 1;
        } else {
            SysAppUser appUser = appUserService.selectSysAppUserByAppUserId(loginUser.getAppUser().getAppUserId());
            return appUser.getPoint();
        }
    }
}
