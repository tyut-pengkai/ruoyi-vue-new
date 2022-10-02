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
import com.ruoyi.system.domain.SysGlobalVariable;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysGlobalVariableService;

import javax.annotation.Resource;

public class GlobalVariableSet extends Function {

    @Resource
    private ISysGlobalVariableService globalVariableService;
    @Resource
    private ValidUtils validUtils;
    @Resource
    private ISysAppUserService appUserService;

    @Override
    public void init() {
        this.setApi(new Api("globalVariableSet.ng", "写远程变量", false, Constants.API_TAG_GENERAL,
                "写远程变量", Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL, new Param[]{
                new Param("variableName", true, "变量名称"),
                new Param("variableValue", true, "变量值"),
                new Param("errorIfNotExist", false, "当变量不存在时是否报错，如果为否则自动创建该名称变量，是传1否传0默认为0"),
                new Param("checkToken", false, "创建的变量是否需要登录才能读写，是传1否传0默认为0"),
                new Param("checkVip", false, "创建的变量是否需要VIP才能读写，是传1否传0默认为0"),
        }, new Resp(Resp.DataType.string, "成功返回0")));
    }

    @Override
    public Object handle() {

        String variableName = this.getParams().get("variableName");
        String variableValue = this.getParams().get("variableValue");
        boolean errorIfNotExist = Convert.toBool(this.getParams().get("errorIfNotExist"), false);
        boolean checkToken = Convert.toBool(this.getParams().get("checkToken"), false);
        boolean checkVip = Convert.toBool(this.getParams().get("checkVip"), false);

        SysGlobalVariable variable = globalVariableService.selectSysGlobalVariableByName(variableName);
        if (variable == null) {
            if (errorIfNotExist) {
                throw new ApiException(ErrorCode.ERROR_GLOBAL_VARIABLE_NOT_EXIST);
            } else {
                variable = new SysGlobalVariable();
                variable.setName(variableName);
                variable.setValue(variableValue);
                variable.setCheckToken(checkToken ? "Y" : "N");
                variable.setCheckVip(checkVip ? "Y" : "N");
                globalVariableService.insertSysGlobalVariable(variable);
                return "0";
            }
        } else {
            // 检查是否登录
            if (UserConstants.YES.equals(variable.getCheckToken())) {
                try {
                    getLoginUser();
                } catch (Exception e) {
                    throw new ApiException(ErrorCode.ERROR_NOT_LOGIN, "写此变量需要用户登录");
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
            variable.setValue(variableValue);
            globalVariableService.updateSysGlobalVariable(variable);
            return "0";
        }
    }
}
