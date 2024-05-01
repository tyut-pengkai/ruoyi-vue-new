package com.ruoyi.api.v1.api.noAuth.code;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.*;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.domain.SysAppUserExpireLog;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.service.*;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class RechargeLoginCode extends Function {

    @Resource
    private ISysAppService appService;
    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private ISysLoginCodeService loginCodeService;
    @Resource
    private ISysConfigService configService;

    @Override
    public void init() {
        this.setApi(new Api("rechargeLoginCode.nc", "登录码充值", false, Constants.API_TAG_CODE,
                "使用登录码充值", new AuthType[]{AuthType.LOGIN_CODE}, Constants.BILL_TYPE_ALL,
                new Param[]{
                        new Param("loginCode", true, "充值到的登录码"),
                        new Param("newLoginCode", true, "用于充值的新登录码"),
                }, new Resp(Resp.DataType.string, "成功返回新的到期时间或点数")));
    }

    @Override
    @Transactional(noRollbackFor = ApiException.class, rollbackFor = Exception.class)
    public Object handle() {

        String key = configService.selectConfigByKey("sys.agent.updateAppUser");
        Integer updateAgentStrategy = Convert.toInt(key, 0);

        String loginCodeStr = this.getParams().get("loginCode");
        SysAppUser appUser = appUserService.selectSysAppUserByAppIdAndLoginCode(this.getApp().getAppId(), loginCodeStr);
        if (appUser == null) {
            throw new ApiException(ErrorCode.ERROR_APP_USER_NOT_EXIST, "被充值用户不存在");
        }
        String newLoginCodeStr = this.getParams().get("newLoginCode");
        SysLoginCode newLoginCode = loginCodeService.selectSysLoginCodeByCardNo(newLoginCodeStr);
        SysAppUserExpireLog expireLog = new SysAppUserExpireLog();
        checkLoginCode(appUser, newLoginCode);
        if (this.getApp().getBillType() == BillType.TIME) {
            expireLog.setExpireTimeBefore(appUser.getExpireTime());
            Date newExpiredTime = MyUtils.getNewExpiredTimeAdd(appUser.getExpireTime(), newLoginCode.getQuota());
            appUser.setExpireTime(newExpiredTime);
            expireLog.setExpireTimeAfter(newExpiredTime);
        } else if (this.getApp().getBillType() == BillType.POINT) {
            expireLog.setPointBefore(appUser.getPoint());
            BigDecimal newPoint = MyUtils.getNewPointAdd(appUser.getPoint(), newLoginCode.getQuota());
            appUser.setPoint(newPoint);
            expireLog.setPointAfter(newPoint);
        }
        appUser.setCardLoginLimitU(newLoginCode.getCardLoginLimitU());
        appUser.setCardLoginLimitM(newLoginCode.getCardLoginLimitM());
        appUser.setCardCustomParams(newLoginCode.getCardCustomParams());
        appUser.setLastChargeCardId(newLoginCode.getCardId());
        appUser.setLastChargeTemplateId(newLoginCode.getTemplateId());

        // 记录用户时长变更日志
        expireLog.setAppUserId(appUser.getAppUserId());
        expireLog.setTemplateId(newLoginCode.getTemplateId());
        expireLog.setCardId(newLoginCode.getCardId());
        expireLog.setChangeDesc("充值卡充值");
        expireLog.setChangeType(AppUserExpireChangeType.RECHARGE);
        expireLog.setChangeAmount(newLoginCode.getQuota());
        expireLog.setCardNo(newLoginCodeStr);
        expireLog.setAppId(this.getApp().getAppId());
        AsyncManager.me().execute(AsyncFactory.recordAppUserExpire(expireLog));

        // 更新用户所属代理
        if (updateAgentStrategy == 0) {
            if (appUser.getAgentId() == null) {
                appUser.setAgentId(newLoginCode.getAgentId());
            }
        } else {
            appUser.setAgentId(newLoginCode.getAgentId());
        }

        newLoginCode.setIsCharged(UserConstants.YES);
        newLoginCode.setChargeTime(DateUtils.getNowDate());
        newLoginCode.setOnSale(UserConstants.NO);
        newLoginCode.setRemark((StringUtils.isNotBlank(newLoginCode.getRemark()) ? newLoginCode.getRemark() + "\n" : "") + "已用于充值单码【" + loginCodeStr + "】");
        newLoginCode.setChargeType(ChargeType.CHARGE);
        newLoginCode.setChargeTo(appUser.getAppUserId());
        loginCodeService.updateSysLoginCode(newLoginCode);
        appUserService.updateSysAppUser(appUser);
        if (this.getApp().getBillType() == BillType.TIME) {
            return DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, appUser.getExpireTime());
        } else if (this.getApp().getBillType() == BillType.POINT) {
            return appUser.getPoint();
        }
        return "";
    }

    private void checkLoginCode(SysAppUser appUser, SysLoginCode loginCode) {
        if (loginCode == null) {
            throw new ApiException(ErrorCode.ERROR_LOGIN_CODE_NOT_EXIST);
        }
        if (!Objects.equals(loginCode.getAppId(), this.getApp().getAppId())) {
            SysApp cardApp = appService.selectSysAppByAppId(loginCode.getAppId());//当前要消耗的卡
            throw new ApiException(ErrorCode.ERROR_CARD_APP_MISMATCH, "所选新单码仅限充值软件 :" + cardApp.getAppName() + " ，不能用于充值软件： " + this.getApp().getAppName());
        }
        if (Objects.equals(loginCode.getIsCharged(), UserConstants.YES)) {
            throw new ApiException(ErrorCode.ERROR_LOGIN_CODE_IS_USED);
        }
        if (!Objects.equals(loginCode.getStatus(), UserConstants.NORMAL)) {
            throw new ApiException(ErrorCode.ERROR_LOGIN_CODE_LOCKED);
        }
        if (loginCode.getExpireTime() != null && loginCode.getExpireTime().before(DateUtils.getNowDate())) {
            throw new ApiException(ErrorCode.ERROR_CARD_EXPIRED, "新单码有效期至：" + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, loginCode.getExpireTime()) + "，此新单码已过期");
        }
    }
}
