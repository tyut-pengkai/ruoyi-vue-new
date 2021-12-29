package com.ruoyi.api.v1.utils;

import com.ruoyi.api.v1.constants.ApiDefine;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.ErrorCode;
import com.ruoyi.common.exception.ApiException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysApp;
import com.ruoyi.system.domain.SysAppVersion;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class ValidUtils {
    /**
     * 不需要验证token的api接口验证
     */
    public void apiCheck(SysApp app, SysAppVersion appVersion, Map<String, String> params, boolean needToken) {
        // 检查api接口是否存在
        checkApiExist(params, false);
        // 检查公共参数
        checkPublicParams(params);
        // 检查该软件是否可调用该api接口
        checkAppMatchApi(app, params);
        // 检查私有参数
        checkPrivateParams(params);
        // 检查数据是否过期
        checkDataExpired(app, params);
        // 检查md5
        checkMd5(appVersion, params);
        // 检查sign
        checkSign(app, params);
        if (needToken) {
            // 检查软件用户是否可用
//        checkAppUserEnable(app, params);
        }
    }

    /**
     * 检查公共参数
     */
    private void checkPublicParams(Map<String, String> params) {
        for (Param publicParam : ApiDefine.publicParams) {
            if (publicParam.isNecessary() && StringUtils.isBlank(params.get(publicParam.getName()))) {
                throw new ApiException(ErrorCode.ERROR_PARAMETERS_MISSING, "缺少必要公共参数或参数值为空：" + publicParam.getName() + "，参数说明：" + publicParam.getDescription());
            }
        }
    }

    /**
     * 检查API是否存在
     */
    private void checkApiExist(Map<String, String> params, Boolean needToken) {
        String api = params.get("api");
        if (needToken != ApiDefine.apiMap.get(api).isCheckToken()) {
            throw new ApiException(ErrorCode.ERROR_API_NOT_EXIST);
        }
    }

    private void checkPrivateParams(Map<String, String> params) {
        Api apiParams = ApiDefine.apiMap.get(params.get("api"));
        if (apiParams.getParams() != null) {
            for (Param privateParam : apiParams.getParams()) {
                if (privateParam.isNecessary() && StringUtils.isBlank(params.get(privateParam.getName()))) {
                    throw new ApiException(ErrorCode.ERROR_PARAMETERS_MISSING, "缺少必要API参数或参数值为空：" + privateParam.getName() + "，参数说明：" + privateParam.getDescription());
                }
            }
        }
    }

    /**
     * 检查md5
     */
    private void checkMd5(SysAppVersion appVersion, Map<String, String> params) {
        boolean flag = false;
        String md5 = appVersion.getMd5();
        if (StringUtils.isNotBlank(md5)) {
            String[] split = md5.split("\\|");
            for (String str : split) {
                if (str.equalsIgnoreCase(params.get("md5"))) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                throw new ApiException(ErrorCode.ERROR_APP_MD5_MISMATCH);
            }
        }
    }

    private void checkAppMatchApi(SysApp app, Map<String, String> params) {
        Api apiParams = ApiDefine.apiMap.get(params.get("api"));
        List<AuthType> loginTypeList = Arrays.asList(apiParams.getAuthTypes());
        List<BillType> chargeTypeList = Arrays.asList(apiParams.getBillTypes());

        if (!(loginTypeList.size() == 0 || loginTypeList.contains(app.getAuthType()))) {
            throw new ApiException(ErrorCode.ERROR_API_CALLED_MISMATCH);
        }
        if (!(chargeTypeList.size() == 0 || chargeTypeList.contains(app.getBillType()))) {
            throw new ApiException(ErrorCode.ERROR_API_CALLED_MISMATCH);
        }
    }

    /**
     * 检查数据包是否过期
     */
    private void checkDataExpired(SysApp app, Map<String, String> params) {
        Long dataTransExpiredTime = app.getDataExpireTime();
        if (dataTransExpiredTime > -1 && (System.currentTimeMillis() - Long.parseLong(params.get("timestamp")) > dataTransExpiredTime * 1000)) {
            throw new ApiException(ErrorCode.ERROR_DATA_EXPIRED);
        }
    }

    /**
     * 检查sign
     */
    private void checkSign(SysApp app, Map<String, String> params) {
//        System.out.println(SignUtil.sign(params, app.getAppSecret()));
        if (!SignUtil.verifySign(params, params.get("sign"), app.getAppSecret())) {
            throw new ApiException(ErrorCode.ERROR_SIGN_INVALID);
        }
    }

//    private void checkAppUserEnable(SysApp app, Map<String, String> params) {
//        Integer loginSoftwareId = getLoginSid();
//        Integer loginUserId = getLoginUid();
//        if (loginSoftwareId == null) {
//            throw new ApiException(Code.ERROR_TOKEN_INVALID);
//        }
//        if (loginSoftwareId != null && software.getId() != Integer.valueOf(loginSoftwareId)) {
//            throw new ApiException(Code.ERROR_REQUEST_CROSS_SOFTWARE);
//        }
//        if (loginSoftwareId != null && loginUserId != null) {
//            SoftwareUser softwareUser = softwareUserService.getBySidAndUid(loginSoftwareId,
//                    getLoginUid());
//            if (softwareUser != null) {
//                if (softwareUser.getIsBanned() == Constants.TRUE) {
//                    throw new ApiException(Code.ERROR_SOFTWARE_USER_LOCKED);
//                }
//                checkSoftwareUserIsExpired(software, softwareUser);
//            } else {
//                throw new ApiException("系统错误");
//            }
//        } else {
//            throw new ApiException(Code.ERROR_NO_LOGIN);
//        }
//    }
}
