package com.ruoyi.api.v1.api.noAuth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.framework.api.v1.utils.ValidUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.domain.SysGlobalVariable;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysGlobalVariableService;

import javax.annotation.Resource;

public class GlobalVariableGet extends Function {

    @Resource
    private ISysGlobalVariableService globalVariableService;
    @Resource
    private ValidUtils validUtils;
    @Resource
    private ISysAppUserService appUserService;
    @Resource
    private TokenService tokenService;

    @Override
    public void init() {
        this.setApi(new Api("globalVariableGet.ng", "读全局变量", false, Constants.API_TAG_GENERAL,
                "读全局变量", Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL, new Param[]{
                new Param("variableName", true, "变量名称"),
                new Param("errorIfNotExist", false, "当变量不存在时是否报错，如果为否则返回空文本，是传1否传0默认为0"),
        }, new Resp(Resp.DataType.string, "变量值")));
    }

    @Override
    public Object handle() {

        String variableName = this.getParams().get("variableName");
        boolean errorIfNotExist = Convert.toBool(this.getParams().get("errorIfNotExist"), false);

        SysGlobalVariable variable = globalVariableService.selectSysGlobalVariableByName(variableName);
        if (variable == null) {
            if (errorIfNotExist) {
                throw new ApiException(ErrorCode.ERROR_GLOBAL_VARIABLE_NOT_EXIST);
            } else {
                return "";
            }
        }

        // 检查是否登录
        if (UserConstants.YES.equals(variable.getCheckToken())) {
            try {
                getLoginUser();
            } catch (Exception e) {
                throw new ApiException(ErrorCode.ERROR_NOT_LOGIN, "获取此变量需要用户登录");
            }
        }
        // 检查是否VIP
        if (UserConstants.YES.equals(variable.getCheckVip())) {
            try {
                // 实时从数据库取软件用户状态，避免使用登录时存储的状态造成数据同步不及时
                SysAppUser appUser = appUserService.selectSysAppUserByAppUserId(getLoginUser().getAppUser().getAppUserId());
                validUtils.checkAppUserIsExpired(getApp(), appUser, true);
            } catch (Exception e) {
                throw new ApiException(ErrorCode.ERROR_NOT_VIP, e.getMessage());
            }
        }
        return variable.getValue();
    }
}
