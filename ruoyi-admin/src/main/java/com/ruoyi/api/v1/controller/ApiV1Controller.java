package com.ruoyi.api.v1.controller;

import com.alibaba.fastjson.JSON;
import com.ruoyi.api.v1.anno.Encrypt;
import com.ruoyi.api.v1.constants.ApiDefine;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.SwaggerVo;
import com.ruoyi.api.v1.service.SwaggerService;
import com.ruoyi.api.v1.service.SysAppLoginService;
import com.ruoyi.api.v1.utils.ValidUtils;
import com.ruoyi.api.v1.utils.encrypt.AesCbcZeroPaddingUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppVersion;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.license.anno.LicenceCheck;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysAppVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

@RequestMapping("/api/v1")
@RestController
@Slf4j
public class ApiV1Controller extends BaseController {

    @Resource
    private ISysAppService appService;
    @Resource
    private ISysAppVersionService appVersionService;
    @Resource
    private SysAppLoginService loginService;
    @Resource
    private ValidUtils validUtils;
    @Resource
    private SwaggerService swaggerService;
    @Resource
    private TokenService tokenService;

    @GetMapping("/swagger")
    @ApiIgnore
    public SwaggerVo swagger(HttpServletRequest request) {
        return swaggerService.getSwaggerInfo(request);
    }

    @GetMapping("/{appkey}")
    public AjaxResult api(@PathVariable("appkey") String appkey) {
        SysApp app = appService.selectSysAppByAppKey(appkey);
        if (app != null) {
            return AjaxResult.success("恭喜您创建软件成功！请通过POST方式根据接口文档说明接入您的软件。");
        }
        return AjaxResult.error("访问有误");
    }

    @LicenceCheck
    @Encrypt(in = true, out = true)
    @PostMapping("/{appkey}")
//    @ApiOperation(value = "API接口", notes = "API接口")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "appkey", value = "AppKey", paramType = "path", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "params", value = "接口需要的参数", paramType = "body", required = true, dataType = "Map")
//    })
    @Log(title = "API调用", businessType = BusinessType.CALL_API)
    public Object api(@PathVariable("appkey") String appkey, @RequestBody Map<String, String> params, HttpServletRequest request) {
        log.info("appkey: {}, 请求参数: {}", appkey, JSON.toJSON(params));
        // 检查软件是否存在
        if (StringUtils.isBlank(appkey)) {
            throw new ApiException(ErrorCode.ERROR_PARAMETERS_MISSING, "AppKey不能为空");
        }
        SysApp app = appService.selectSysAppByAppKey(appkey);
        // API校验
        String api = params.get("api").trim();
        try {
            if (StringUtils.isNotBlank(app.getApiPwd())) {
                api = AesCbcZeroPaddingUtil.decode(api, app.getApiPwd());
                api = api != null ? api.trim() : "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException("匿名API解密发生错误，如果设置了API匿名密码，请使用加密后的API名称替换原API名称");
        }
        if (!ApiDefine.apiMap.containsKey(api)) {
            throw new ApiException(ErrorCode.ERROR_API_NOT_EXIST);
        }
        Api apii = ApiDefine.apiMap.get(api);
        if (apii.isCheckToken()) {
            LoginUser loginUser = tokenService.getLoginUser(request);
            if (loginUser == null) {
                throw new ApiException(ErrorCode.ERROR_UNAUTHORIZED);
            }
        }
        validUtils.apiCheck(api, app, params, apii.isCheckToken());
        String appSecret = params.get("app_secret");
        if (!Objects.equals(app.getAppSecret(), appSecret)) {
            throw new ApiException(ErrorCode.ERROR_APPKEY_OR_APPSECRET_ERROR);
        }
        String deviceCode = params.get("dev_code");
        SysAppVersion version = null;
        switch (api) {
            case "login.nu":
                // 检查软件版本是否存在
                version = validUtils.apiCheckPreLogin(appkey, app, params);
                if (app.getAuthType() == AuthType.ACCOUNT) { // by account
                    String username = params.get("username");
                    String password = params.get("password");
                    // 调用登录接口
                    return loginService.appLogin(username, password, app, version, deviceCode);
                } else {
                    throw new ApiException(ErrorCode.ERROR_API_CALLED_MISMATCH);
                }
            case "login.nc":
                // 检查软件版本是否存在
                version = validUtils.apiCheckPreLogin(appkey, app, params);
                if (app.getAuthType() == AuthType.LOGIN_CODE) { // by login code
                    String loginCode = params.get("login_code");
                    // 调用登录接口
                    return loginService.appLogin(loginCode, app, version, deviceCode);
                } else {
                    throw new ApiException(ErrorCode.ERROR_API_CALLED_MISMATCH);
                }
            case "logout.ag":
                LoginUser loginUser = getLoginUser();
                return loginService.appLogout(loginUser);
            default:
                Function function = ApiDefine.functionMap.get(api);
                function.setApp(app);
                function.setParams(params);
                return function.handle();
        }
    }
}
