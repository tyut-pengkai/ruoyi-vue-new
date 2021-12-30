package com.ruoyi.api.v1.controller;

import com.alibaba.fastjson.JSON;
import com.ruoyi.api.v1.service.SysAppLoginService;
import com.ruoyi.api.v1.utils.ValidUtils;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppVersion;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.BindType;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysAppVersionService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

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

    //    @Encrypt(in = true, out = true)
    @PostMapping("/{appkey}/login")
    @ApiOperation(value = "用户登录", notes = "用户登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appkey", value = "AppKey", paramType = "path", required = true, dataType = "String"),
            @ApiImplicitParam(name = "params", value = "接口需要的参数", paramType = "body", required = true, dataType = "Map")
    })
    public String login(@PathVariable("appkey") String appkey, @RequestBody Map<String, String> params) {
        log.info("appkey: {}, 请求参数: {}", appkey, JSON.toJSON(params));
        // 软件校验
        SysApp app = appService.selectSysAppByAppKey(appkey);
        if (app == null) {
            log.info("软件：{} 不存在.", appkey);
            throw new ApiException(ErrorCode.ERROR_APP_NOT_EXIST);
        } else if (UserStatus.DELETED.getCode().equals(app.getDelFlag())) {
            log.info("软件：{} 已被删除.", app.getAppName());
            throw new ApiException(ErrorCode.ERROR_ACCOUNT_NOT_EXIST, "软件：" + app.getAppName() + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(app.getStatus())) {
            log.info("软件：{} 已被停用.", app.getAppName());
            throw new ApiException(ErrorCode.ERROR_APP_OFF, "软件：" + app.getAppName() + " 已停用");
        }
        // 软件版本校验
        long version = Long.parseLong(params.get("app_version"));
        SysAppVersion appVersion = appVersionService.selectSysAppVersionByAppIdAndVersion(app.getAppId(), version);
        if (appVersion == null) {
            log.info("软件 {} 的版本：{} 不存在.", app.getAppName(), version);
            throw new ApiException(ErrorCode.ERROR_APP_VERSION_NOT_EXIST);
        } else if (UserStatus.DELETED.getCode().equals(appVersion.getDelFlag())) {
            log.info("软件 {} 的版本：{} 已被删除.", app.getAppName(), version);
            throw new ApiException(ErrorCode.ERROR_APP_VERSION_NOT_EXIST, "软件版本：" + version + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(appVersion.getStatus())) {
            log.info("软件：{} 的版本：{} 已被停用.", app.getAppName(), version);
            throw new ApiException(ErrorCode.ERROR_APP_VERSION_OFF, "软件版本：" + version + " 已停用");
        }
        // 设备码校验
        String deviceCode = params.get("device_code");
        if (app.getBindType() != null && app.getBindType() != BindType.NONE) {
            if (StringUtils.isBlank(deviceCode)) {
                throw new ApiException(ErrorCode.ERROR_PARAMETERS_MISSING, "软件已开启设备验证，设备码不能为空");
            }
        }
        if (app.getAuthType() == AuthType.ACCOUNT) { // by account
            String username = params.get("username");
            String password = params.get("password");
            if (StringUtils.isAnyBlank(username, password)) {
                throw new ApiException(ErrorCode.ERROR_PARAMETERS_MISSING, "账号或密码不能为空");
            }
            // 调用登录接口
            return loginService.appLogin(username, password, app, appVersion, deviceCode);
//        } else { // by login code
//            String loginCode = params.get("logincode");
//            String codePwd = params.get("codepwd");
//            String machineCode = params.get("mcode");
//            String comments = params.get("c");
//            if(StringUtils.isBlank(loginCode)) {
//                throw new ApiException(Code.ERROR_PARAMETERS_MISSING, "登录码不能为空");
//            }
//            if (software.getBindType() != null && software.getBindType() != BindType.NONE) {
//                if (StringUtils.isBlank(machineCode)) {
//                    throw new ApiException(Code.ERROR_PARAMETERS_MISSING, "设备码不能为空");
//                }
//            }
//            // 调用登录接口
//            String token = apiV1LoginCodeService.login(request, loginCode, codePwd, machineCode, software.getId(),
//                    loginPosition, comments);
//            return token;
        }
        return null;
    }


    //    @Encrypt(in = true, out = true)
    @PostMapping("/{appkey}")
    @ApiOperation(value = "API接口", notes = "API接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appkey", value = "AppKey", paramType = "path", required = true, dataType = "String"),
            @ApiImplicitParam(name = "params", value = "接口需要的参数", paramType = "body", required = true, dataType = "Map")
    })
    public Object api(@PathVariable("appkey") String appkey, @RequestBody Map<String, String> params) {
        log.info("appkey: {}, 请求参数: {}", appkey, JSON.toJSON(params));

        // 检查软件是否存在
        if (StringUtils.isBlank(appkey)) {
            throw new ApiException(ErrorCode.ERROR_PARAMETERS_MISSING, "AppKey不能为空");
        }
        // 检查软件版本是否存在
        String appVersionStr = params.get("app_version");
        if (StringUtils.isBlank(appVersionStr)) {
            throw new ApiException(ErrorCode.ERROR_PARAMETERS_MISSING, "软件版本号不能为空");
        }
        // 检查软件是否存在
        SysApp app = appService.selectSysAppByAppKey(appkey);
        if (app == null) {
            throw new ApiException(ErrorCode.ERROR_APP_NOT_EXIST);
        }
        // 检查软件版本是否存在
        SysAppVersion appVersion = appVersionService.selectSysAppVersionByAppIdAndVersion(app.getAppId(), Long.parseLong(appVersionStr));
        if (appVersion == null) {
            throw new ApiException(ErrorCode.ERROR_APP_VERSION_NOT_EXIST);
        }
        // API校验
        validUtils.apiCheck(app, appVersion, params, false);

        System.out.println(JSON.toJSONString(getLoginUser()));
        if ("time".equals(params.get("api"))) {
            return DateUtils.getTime();
        } else if ("testNoToken".equals(params.get("api"))) {

            return params;
        }
        return params;
    }

}
