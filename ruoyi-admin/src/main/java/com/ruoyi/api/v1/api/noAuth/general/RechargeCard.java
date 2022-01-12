package com.ruoyi.api.v1.api.noAuth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.system.service.ISysAppService;

import javax.annotation.Resource;

public class RechargeCard extends Function {

    @Resource
    private ISysAppService appService;

    @Override
    public void init() {
        this.setApi(new Api("rechargeCard.ng", "充值卡充值（此接口暂未实现）", false, Constants.API_TAG_GENERAL,
                "使用充值卡充值", new AuthType[]{AuthType.ACCOUNT}, Constants.BILL_TYPE_ALL,
                new Param[]{
                        new Param("username", true, "充值用户"),
                        new Param("password", false, "充值用户密码"),
                        new Param("validPassword", false, "是否验证充值用户密码，防止充错用户，验证传1，不验证传0，默认为0"),
                        new Param("cardNo", true, "充值卡号"),
                        new Param("cardPassword", true, "充值卡密码")
                }));
    }

    @Override
    public Object handle() {
        return null;
    }
}
