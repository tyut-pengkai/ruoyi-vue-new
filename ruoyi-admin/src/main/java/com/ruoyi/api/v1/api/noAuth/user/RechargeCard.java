package com.ruoyi.api.v1.api.noAuth.user;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.ChargeRule;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysCard;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysCardService;
import com.ruoyi.system.service.ISysUserService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class RechargeCard extends Function {

    @Resource
    private ISysAppService appService;
    @Resource
    private ISysUserService userService;
    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private ISysCardService cardService;

    @Override
    public void init() {
        this.setApi(new Api("rechargeCard.nu", "充值卡充值", false, Constants.API_TAG_ACCOUNT,
                "使用充值卡充值", new AuthType[]{AuthType.ACCOUNT}, Constants.BILL_TYPE_ALL,
                new Param[]{
                        new Param("username", true, "充值用户"),
                        new Param("password", false, "充值用户密码"),
                        new Param("validPassword", false, "是否验证充值用户密码，防止充错用户，验证传1，不验证传0，默认为0"),
                        new Param("cardNo", true, "充值卡号"),
                        new Param("cardPassword", false, "充值卡密码")
                }, new Resp(Resp.DataType.string, "成功返回新的到期时间或点数")));
    }

    @Override
    @Transactional(noRollbackFor = ApiException.class, rollbackFor = Exception.class)
    public Object handle() {
        String username = this.getParams().get("username");
        SysUser user = userService.selectUserByUserName(username);
        if (user == null) {
            throw new ApiException(ErrorCode.ERROR_ACCOUNT_NOT_EXIST);
        }
        if (this.getParams().get("validPassword").equals("1")) {
            String password = this.getParams().get("password");
            if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
                throw new ApiException(ErrorCode.ERROR_USERNAME_OR_PASSWORD_ERROR);
            }
        }
        SysAppUser appUser = appUserService.selectSysAppUserByAppIdAndUserId(this.getApp().getAppId(), user.getUserId());
        if (appUser == null) {
            throw new ApiException(ErrorCode.ERROR_APP_USER_NOT_EXIST, "该账号未登录过所充值软件，请至少登录一次后重试");
        }
        String cardNo = this.getParams().get("cardNo");
        String cardPwd = this.getParams().get("cardPassword");
        SysCard card = cardService.selectSysCardByCardNo(cardNo);
        checkCard(appUser, card, cardPwd);
        if (this.getApp().getBillType() == BillType.TIME) {
            Date newExpiredTime = MyUtils.getNewExpiredTimeAdd(appUser.getExpireTime(), card.getQuota());
            appUser.setExpireTime(newExpiredTime);
        } else if (this.getApp().getBillType() == BillType.POINT) {
            BigDecimal newPoint = MyUtils.getNewPointAdd(appUser.getPoint(), card.getQuota());
            appUser.setPoint(newPoint);
        }
        appUser.setCardLoginLimitU(card.getCardLoginLimitU());
        appUser.setCardLoginLimitM(card.getCardLoginLimitM());
        appUser.setCardCustomParams(card.getCardCustomParams());
        appUser.setLastChargeCardId(card.getCardId());
        appUser.setLastChargeTemplateId(card.getTemplateId());
        card.setIsCharged(UserConstants.YES);
        card.setChargeTime(DateUtils.getNowDate());
        card.setOnSale(UserConstants.NO);
        card.setRemark((StringUtils.isNotBlank(card.getRemark()) ? card.getRemark() + "\n" : "") + "已用于充值用户【" + username + "】");
        cardService.updateSysCard(card);
        appUserService.updateSysAppUser(appUser);
        if (this.getApp().getBillType() == BillType.TIME) {
            return DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, appUser.getExpireTime());
        } else if (this.getApp().getBillType() == BillType.POINT) {
            return appUser.getPoint();
        }
        return "";
    }

    private void checkCard(SysAppUser appUser, SysCard card, String cardPwd) {
        if (card == null) {
            throw new ApiException(ErrorCode.ERROR_CARD_NOT_EXIST);
        }
        if (!Objects.equals(card.getAppId(), this.getApp().getAppId())) {
            SysApp cardApp = appService.selectSysAppByAppId(card.getAppId());//当前要消耗的卡
            throw new ApiException(ErrorCode.ERROR_CARD_APP_MISMATCH, "所选充值卡仅限充值软件 :" + cardApp.getAppName() + " ，不能用于充值软件： " + this.getApp().getAppName());
        }
        if (Objects.equals(card.getIsCharged(), UserConstants.YES)) {
            throw new ApiException(ErrorCode.ERROR_CARD_IS_USED);
        }
        if (!Objects.equals(card.getStatus(), UserConstants.NORMAL)) {
            throw new ApiException(ErrorCode.ERROR_CARD_LOCKED);
        }
        if (card.getExpireTime() != null && card.getExpireTime().before(DateUtils.getNowDate())) {
            throw new ApiException(ErrorCode.ERROR_CARD_EXPIRED, "充值卡有效期至：" + DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, card.getExpireTime()) + "，此充值卡已过期");
        }
        if (card.getChargeRule() == ChargeRule.EXPIRE_REQUIRED) {
            if (this.getApp().getBillType() == BillType.TIME) {
                if (appUser.getExpireTime() != null && appUser.getExpireTime().after(DateUtils.getNowDate())) {
                    throw new ApiException(ErrorCode.ERROR_CHARGE_RULE_MISMATCH, "此充值卡仅限为已过期用户充值，当前用户尚未过期");
                }
            } else if (this.getApp().getBillType() == BillType.POINT) {
                if (appUser.getPoint() != null && appUser.getPoint().compareTo(BigDecimal.ZERO) > 0) {
                    throw new ApiException(ErrorCode.ERROR_CHARGE_RULE_MISMATCH, "此充值卡仅限为点数小于等于0的用户充值，当前用户点数大于0");
                }
            } else {
                throw new ApiException("软件未指定计费方式");
            }
        }
        if (!(StringUtils.isBlank(card.getCardPass()) && StringUtils.isBlank(cardPwd)) && !card.getCardPass().equals(cardPwd)) {
            throw new ApiException(ErrorCode.ERROR_CARD_PASSWORD_MISMATCH);
        }
    }
}
