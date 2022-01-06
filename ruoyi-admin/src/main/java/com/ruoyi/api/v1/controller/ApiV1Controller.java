package com.ruoyi.api.v1.controller;

import com.alibaba.fastjson.JSON;
import com.ruoyi.api.v1.anno.Encrypt;
import com.ruoyi.api.v1.domain.SwaggerVo;
import com.ruoyi.api.v1.service.SwaggerService;
import com.ruoyi.api.v1.service.SysAppLoginService;
import com.ruoyi.api.v1.utils.ValidUtils;
import com.ruoyi.api.v1.utils.encrypt.AesCbcZeroPaddingUtil;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppVersion;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.ErrorCode;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    @Resource
    private SwaggerService swaggerService;

    @GetMapping("/swagger")
    @ApiIgnore
    public SwaggerVo swagger(HttpServletRequest request) {
        return swaggerService.getSwaggerInfo(request);
    }

    @Encrypt(in = true, out = true)
    @PostMapping("/{appkey}/noAuth")
    @ApiOperation(value = "API接口", notes = "API接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appkey", value = "AppKey", paramType = "path", required = true, dataType = "String"),
            @ApiImplicitParam(name = "params", value = "接口需要的参数", paramType = "body", required = true, dataType = "Map")
    })
    public Object noAuthApi(@PathVariable("appkey") String appkey, @RequestBody Map<String, String> params) {
        log.info("appkey: {}, 请求参数: {}", appkey, JSON.toJSON(params));
        // 检查软件是否存在
        if (StringUtils.isBlank(appkey)) {
            throw new ApiException(ErrorCode.ERROR_PARAMETERS_MISSING, "AppKey不能为空");
        }
        // 检查软件版本是否存在
        String appVersionStr = params.get("app_ver");
        if (StringUtils.isBlank(appVersionStr)) {
            throw new ApiException(ErrorCode.ERROR_PARAMETERS_MISSING, "软件版本号不能为空");
        }
        SysApp app = appService.selectSysAppByAppKey(appkey);
        Long version = Long.parseLong(appVersionStr);
        SysAppVersion appVersion = appVersionService.selectSysAppVersionByAppIdAndVersion(app.getAppId(), version);
        // API校验
        validUtils.apiCheckAppAndVersion(appkey, app, version, appVersion);
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
        validUtils.apiCheck(api, app, appVersion, params, false);
        String deviceCode = params.get("dev_code");

        if ("login".equals(api)) {
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
        } else if ("time".equals(api)) {
            return DateUtils.getTime();
        } else if ("testNoToken".equals(api)) {
            return params;
        } else if ("latestVersion".equals(api)) {
            return appVersionService.selectLatestVersionByAppId(app.getAppId());
        }
        return AjaxResult.error();
    }


    @Encrypt(in = true, out = true)
    @PostMapping("/{appkey}/auth")
    @ApiOperation(value = "API接口", notes = "API接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "token", paramType = "header", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appkey", value = "AppKey", paramType = "path", required = true, dataType = "String"),
            @ApiImplicitParam(name = "params", value = "接口需要的参数", paramType = "body", required = true, dataType = "Map")
    })
    public Object authApi(@PathVariable("appkey") String appkey, @RequestBody Map<String, String> params) {
        log.info("appkey: {}, 请求参数: {}", appkey, JSON.toJSON(params));
        // 检查软件是否存在
        if (StringUtils.isBlank(appkey)) {
            throw new ApiException(ErrorCode.ERROR_PARAMETERS_MISSING, "AppKey不能为空");
        }
        // 检查软件版本是否存在
        String appVersionStr = params.get("app_ver");
        if (StringUtils.isBlank(appVersionStr)) {
            throw new ApiException(ErrorCode.ERROR_PARAMETERS_MISSING, "软件版本号不能为空");
        }
        SysApp app = appService.selectSysAppByAppKey(appkey);
        Long version = Long.parseLong(appVersionStr);
        SysAppVersion appVersion = appVersionService.selectSysAppVersionByAppIdAndVersion(app.getAppId(), version);
        // API校验
        validUtils.apiCheckAppAndVersion(appkey, app, version, appVersion);
        String api = params.get("api").trim();
        try {
            if (StringUtils.isNotBlank(app.getApiPwd())) {
                api = AesCbcZeroPaddingUtil.decode(api, app.getApiPwd()).trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException("匿名API解密发生错误，如果设置了API匿名密码，请使用加密后的API名称替换原API名称");
        }
        validUtils.apiCheck(api, app, appVersion, params, true);
        System.out.println(JSON.toJSONString(getLoginUser()));
//        String deviceCode = params.get("dev_code");

        if ("times".equals(api)) {
            return DateUtils.getTime();
        } else if ("testToken".equals(api)) {
            return params;
        }
        return AjaxResult.error();
    }

}
