package com.ruoyi.api.v1.constants;

import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiDefine {
    public static List<Param> publicParamsAuth = new ArrayList<>();
    public static List<Param> publicParamsNoAuth = new ArrayList<>();
    //	例：
//	{
//		ApiParams api = new ApiParams("login", "用户登录", //
//				Constants.LOGIN_TYPE_ALL, Constants.CHARGE_TYPE_ALL);
//		List<Param> params = new ArrayList<>();
//		params.add(new Param("username", true, "用户名"));
//		params.add(new Param("password", true, "密码"));
//		params.add(new Param("mcode", false, "设备码"));
//		params.add(new Param("c", false, "备注信息"));
//		api.setParams(params);
//		privateParamsNoTokenMap.put(api.getApi(), api);
////	或者：
//		api = new ApiParams("login", "用户登录", //
//				new Param[] { //
//						new Param("username", true, "用户名"), //
//						new Param("password", true, "密码"), //
//						new Param("mcode", false, "设备码"), //
//						new Param("c", false, "备注信息")//
//				});
//		privateParamsNoTokenMap.put(api.getApi(), api);
//	}
    public static Map<String, Api> apiMap = new HashMap<>();

    static {
        publicParamsAuth.add(new Param("app_ver", true, "软件版本号"));
        publicParamsAuth.add(new Param("api", true, "请求的API接口，此处为${api}"));
        publicParamsAuth.add(new Param("dev_code", false, "设备码，如果开启设备绑定，则必须提供"));
        publicParamsAuth.add(new Param("app_secret", true, "AppSecret"));
        publicParamsAuth.add(new Param("md5", false, "软件MD5"));
        publicParamsAuth.add(new Param("vstr", false, "用作标记或验证的冗余数据，将原样返回"));
        publicParamsAuth.add(new Param("timestamp", true, "13位时间戳（精确到毫秒）"));
        publicParamsAuth.add(new Param("sign", true, "数据签名"));
        // ===================================
        publicParamsNoAuth.add(new Param("app_ver", true, "软件版本号"));
        publicParamsNoAuth.add(new Param("api", true, "请求的API接口，此处为${api}"));
        publicParamsNoAuth.add(new Param("dev_code", false, "设备码，如果开启设备绑定，则必须提供"));
        publicParamsNoAuth.add(new Param("app_secret", true, "AppSecret"));
    }

    static {
        Api[] apis = new Api[]{ //
                // 调试工具
                new Api("calcSign", "计算SIGN值", false, Constants.API_TAG_DEBUG_TOOL, "计算SIGN值"), //
                // noAuth
//                new Api("testNoToken", "测试非登录接口", false, Constants.API_TAG_COMMON, "测试noToken接口"), //
                new Api("userLogin", "账号登录", false, Constants.API_TAG_ACCOUNT, "账号登录接口",
                        new Param[]{
                                new Param("username", true, "账号"), //
                                new Param("password", true, "密码"), //
                        }), //
                new Api("codeLogin", "登录码登录", false, Constants.API_TAG_CODE, "登录码登录接口",
                        new Param[]{
                                new Param("login_code", true, "登录码"), //
                        }), //

                new Api("time", "获取服务器时间", false, Constants.API_TAG_COMMON, "获取服务器时间，格式yyyy-MM-dd HH:mm:ss"), //
                new Api("latestVersion", "获取软件最新版本", false, Constants.API_TAG_COMMON, "获取软件最新版本"), //

                // Auth
//                new Api("testToken", "测试登录接口", true, Constants.API_TAG_COMMON, "测试token接口"), //
                new Api("logout", "注销登录", true, Constants.API_TAG_COMMON, "注销登录接口"), //
        };
        for (Api api : apis) {
            apiMap.put(api.getApi(), api);
        }
    }
}
