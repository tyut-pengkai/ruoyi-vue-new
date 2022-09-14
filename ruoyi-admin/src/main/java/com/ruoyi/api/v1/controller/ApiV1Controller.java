package com.ruoyi.api.v1.controller;

import com.alibaba.fastjson.JSON;
import com.ruoyi.api.v1.anno.Encrypt;
import com.ruoyi.api.v1.constants.ApiDefine;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.SwaggerVo;
import com.ruoyi.api.v1.service.SwaggerService;
import com.ruoyi.api.v1.utils.encrypt.AesCbcZeroPaddingUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.*;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.api.v1.service.SysAppLoginService;
import com.ruoyi.framework.api.v1.utils.ValidUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.service.*;
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
    private ISysAppUserService appUserService;
    @Resource
    private ISysAppTrialUserService appTrialUserService;
    @Resource
    private SysAppLoginService loginService;
    @Resource
    private ValidUtils validUtils;
    @Resource
    private SwaggerService swaggerService;
    @Resource
    private TokenService tokenService;
    @Resource
    private ISysLoginCodeService loginCodeService;
    @Resource
    private ISysDeviceCodeService deviceCodeService;
    @Resource
    private ISysAppUserDeviceCodeService appUserDeviceCodeService;
    @Resource
    private ISysUserService userService;

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

//    @LicenceCheck
    @Encrypt(in = true, out = true)
    @PostMapping("/{appkey}")
//    @ApiOperation(value = "API接口", notes = "API接口")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "appkey", value = "AppKey", paramType = "path", required = true, dataType = "String"),
//            @ApiImplicitParam(name = "params", value = "接口需要的参数", paramType = "body", required = true, dataType = "Map")
//    })
    @Log(title = "WEB API", businessType = BusinessType.CALL_API)
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
            if (loginUser == null || !loginUser.getIfApp()) {
                String token = tokenService.getToken(request);
                String uuid = tokenService.tokenToUuid(token);
                if (Constants.LAST_ERROR_REASON_MAP.containsKey(uuid)) {
                    String tip = Constants.LAST_ERROR_REASON_MAP.get(uuid);
//                    Constants.LAST_ERROR_REASON_MAP.remove(uuid);
                    throw new ApiException(ErrorCode.ERROR_UNAUTHORIZED, tip);
                } else {
                    throw new ApiException(ErrorCode.ERROR_UNAUTHORIZED);
                }
            }
            // 检测用户状态是否正常
            if (loginUser.getIfApp()) {
                // 检查用户是否是当前软件的用户
                if (loginUser.getIfTrial()) {
                    SysAppTrialUser appUser = appTrialUserService.selectSysAppTrialUserByAppTrialUserId(loginUser.getAppTrialUser().getAppTrialUserId());
                    // 检查用户是否过期
                    if (!validUtils.checkInTrialQuantum(app)) {
                        validUtils.checkAppTrialUserIsExpired(appUser);
                    }
                    // 检查软件用户是否被封禁
                    if (UserStatus.DISABLE.getCode().equals(appUser.getStatus())) {
                        throw new ApiException(ErrorCode.ERROR_APP_TRIAL_USER_LOCKED, "您的试用已被停用");
                    }
                } else {
                    if (!loginUser.getAppUser().getAppId().equals(app.getAppId())) {
                        throw new ApiException(ErrorCode.ERROR_LOGIN_USER_APP_MISMATCH);
                    }
                    // 检查用户是否过期
                    SysAppUser appUser = appUserService.selectSysAppUserByAppUserId(loginUser.getAppUser().getAppUserId());
                    validUtils.checkAppUserIsExpired(app, appUser);
                    // 检查用户是否被封禁
                    if (loginUser.getApp().getAuthType() == AuthType.ACCOUNT) {
                        Long userId = loginUser.getUser().getUserId();
                        SysUser user = userService.selectUserById(userId);
                        validUtils.checkUser(loginUser.getUsername(), user);
                    } else if (app.getAuthType() == AuthType.LOGIN_CODE) {
                        String loginCodeStr = loginUser.getAppUser().getLoginCode();
                        SysLoginCode loginCode = loginCodeService.selectSysLoginCodeByCardNo(loginCodeStr);
                        validUtils.checkLoginCode(app, loginCodeStr, loginCode);
                    }
                    // 检查软件用户是否被封禁
                    if (UserStatus.DISABLE.getCode().equals(appUser.getStatus())) {
                        if (app.getAuthType() == AuthType.ACCOUNT) {
                            throw new ApiException(ErrorCode.ERROR_APP_USER_LOCKED, "用户：" + appUser.getUserName() + " 已停用");
                        } else if (app.getAuthType() == AuthType.LOGIN_CODE) {
                            throw new ApiException(ErrorCode.ERROR_APP_USER_LOCKED, "单码：" + appUser.getLoginCode() + " 已停用");
                        }
                    }
                    // 检查用户设备是否被禁用
                    SysDeviceCode deviceCode = loginUser.getDeviceCode();
                    if (deviceCode != null && StringUtils.isNotBlank(deviceCode.getDeviceCode())) {
                        SysDeviceCode code = deviceCodeService.selectSysDeviceCodeByDeviceCode(deviceCode.getDeviceCode());
                        if (UserStatus.DISABLE.getCode().equals(code.getStatus())) {
                            log.info("用户设备：{} 已被停用所有软件.", deviceCode.getDeviceCode());
                            throw new ApiException(ErrorCode.ERROR_DEVICE_CODE_LOCKED, "用户设备：" + deviceCode.getDeviceCode() + " 已被停用所有软件");
                        }
                        SysAppUserDeviceCode appUserDeviceCode = appUserDeviceCodeService.selectSysAppUserDeviceCodeByAppUserIdAndDeviceCodeId(appUser.getAppUserId(), deviceCode.getDeviceCodeId());
                        if (UserStatus.DISABLE.getCode().equals(appUserDeviceCode.getStatus())) {
                            log.info("用户设备：{} 已被停用当前软件.", deviceCode.getDeviceCode());
                            throw new ApiException(ErrorCode.ERROR_DEVICE_CODE_LOCKED, "用户设备：" + deviceCode.getDeviceCode() + " 已被停用当前软件");
                        }
                    }
                }
            }
        }
        validUtils.apiCheck(api, app, params, apii.isCheckToken());
        String appSecret = params.get("appSecret");
        if (!Objects.equals(app.getAppSecret(), appSecret)) {
            throw new ApiException(ErrorCode.ERROR_APPKEY_OR_APPSECRET_ERROR);
        }
        String deviceCode = params.get("deviceCode");
        SysAppVersion version = null;
        switch (api) {
            case "login.nu":
                // 检查软件版本是否存在
                version = validUtils.apiCheckPreLogin(appkey, app, params);
                if (app.getAuthType() == AuthType.ACCOUNT) { // by account
                    String username = params.get("username");
                    String password = params.get("password");
                    boolean autoReducePoint = Convert.toBool(params.get("autoReducePoint"), true);
                    // 调用登录接口
                    return loginService.appLogin(username, password, app, version, deviceCode, autoReducePoint);
                } else {
                    throw new ApiException(ErrorCode.ERROR_API_CALLED_MISMATCH);
                }
            case "login.nc":
                // 检查软件版本是否存在
                version = validUtils.apiCheckPreLogin(appkey, app, params);
                if (app.getAuthType() == AuthType.LOGIN_CODE) { // by login code
                    String loginCode = params.get("loginCode");
                    boolean autoReducePoint = Convert.toBool(params.get("autoReducePoint"), true);
                    // 调用登录接口
                    return loginService.appLogin(loginCode, app, version, deviceCode, autoReducePoint);
                } else {
                    throw new ApiException(ErrorCode.ERROR_API_CALLED_MISMATCH);
                }
            case "trialLogin.ng":
                // 检查软件版本是否存在
                version = validUtils.apiCheckPreLogin(appkey, app, params);
                String ip = IpUtils.getIpAddr(request);
                return loginService.appTrialLogin(ip, app, version, deviceCode);
            case "logout.ag":
                LoginUser loginUser = getLoginUser();
                return "注销成功".equals(loginService.appLogout(loginUser)) ? "0" : "1";
            default:
                Function function = ApiDefine.functionMap.get(api);
                function.setApp(app);
                function.setParams(params);
                return function.handle();
        }
    }
}
