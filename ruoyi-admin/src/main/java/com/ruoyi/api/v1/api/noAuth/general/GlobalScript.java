package com.ruoyi.api.v1.api.noAuth.general;

import com.ruoyi.api.v1.constants.Constants;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.api.v1.utils.ScriptUtils;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.framework.api.v1.utils.ValidUtils;
import com.ruoyi.system.domain.SysGlobalScript;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysGlobalScriptService;

import javax.annotation.Resource;

public class GlobalScript extends Function {

    @Resource
    private ISysGlobalScriptService globalScriptService;
    @Resource
    private ValidUtils validUtils;
    @Resource
    private ISysAppUserService appUserService;

    @Override
    public void init() {
        this.setApi(new Api("globalScript.ng", "执行远程脚本", false, Constants.API_TAG_GENERAL,
                "执行远程脚本", Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL, new Param[]{
                new Param("scriptKey", true, "脚本Key"),
                new Param("scriptParams", false, "脚本参数"),
        }, new Resp(Resp.DataType.string, "执行结果")));
    }

    @Override
    public Object handle() {

        String scriptKey = this.getParams().get("scriptKey");
        String scriptParams = this.getParams().get("scriptParams");

        SysGlobalScript script = globalScriptService.selectSysGlobalScriptByScriptKey(scriptKey);
        if (script == null) {
            throw new ApiException(ErrorCode.ERROR_GLOBAL_SCRIPT_NOT_EXIST);
        }

        // 检查是否登录
        if (UserConstants.YES.equals(script.getCheckToken())) {
            try {
                getLoginUser();
            } catch (Exception e) {
                throw new ApiException(ErrorCode.ERROR_NOT_LOGIN, "调用此脚本需要用户登录");
            }
        }
        // 检查是否VIP
        if (UserConstants.YES.equals(script.getCheckVip())) {
            try {
                // 实时从数据库取软件用户状态，避免使用登录时存储的状态造成数据同步不及时
                SysAppUser appUser = appUserService.selectSysAppUserByAppUserId(getLoginUser().getAppUserId());
                validUtils.checkAppUserIsExpired(getApp(), appUser, true);
            } catch (Exception e) {
                throw new ApiException(ErrorCode.ERROR_NOT_VIP, e.getMessage());
            }
        }

        // 渲染脚本
        String scriptContent = script.getContent();
        scriptContent = ScriptUtils.renderScriptContent(scriptContent, this);

        return ScriptUtils.exec(scriptContent, script.getLanguage(), scriptParams);
    }
}
