package com.ruoyi.api.v1.constants;

import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Param;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiDefine {
	public static List<Param> publicParams = new ArrayList<>();
	//	例：
//	{
//		ApiParams api = new ApiParams("login", "用户登录", //
//				Constants.LOGIN_TYPE_ALL, Constants.CHARGE_TYPE_ALL);
//		List<Param> params = new ArrayList<>();
//		params.add(new Param("username", true, "用户名"));
//		params.add(new Param("password", true, "密码"));
//		params.add(new Param("mcode", false, "机器码"));
//		params.add(new Param("c", false, "备注信息"));
//		api.setParams(params);
//		privateParamsNoTokenMap.put(api.getApi(), api);
////	或者：
//		api = new ApiParams("login", "用户登录", //
//				new Param[] { //
//						new Param("username", true, "用户名"), //
//						new Param("password", true, "密码"), //
//						new Param("mcode", false, "机器码"), //
//						new Param("c", false, "备注信息")//
//				});
//		privateParamsNoTokenMap.put(api.getApi(), api);
//	}
	public static Map<String, Api> apiMap = new HashMap<>();

	static {
		publicParams.add(new Param("api", true, "请求的API接口"));
		publicParams.add(new Param("timestamp", true, "13位时间戳（精确到毫秒）"));
		publicParams.add(new Param("sign", true, "数据签名"));
		publicParams.add(new Param("md5", false, "软件MD5"));
		publicParams.add(new Param("vstr", false, "用作标记或验证的冗余数据，将原样返回"));
	}

	static {
		Api[] apis = new Api[]{ //
				new Api("login", "用户登录", false, Constants.API_TAG_COMMON, "用户登录接口", new Param[]{
						new Param("username", false, "用户名"), //
						new Param("password", false, "密码"), //
						new Param("logincode", false, "登录码（用户名密码与登录码二选一）"), //
						new Param("device_code", false, "设备码"), //
						new Param("c", false, "备注信息")//
				}), //
				new Api("logout", "用户登出", true, Constants.API_TAG_COMMON, "用户登出接口"), //
				new Api("testNoToken", "测试非登录接口", false, Constants.API_TAG_COMMON, "测试noToken接口"), //
				new Api("time", "获取服务器时间", false, Constants.API_TAG_COMMON, "获取服务器时间，格式yyyy-MM-dd HH:mm:ss"), //
				new Api("testToken", "测试登录接口", true, Constants.API_TAG_ACCOUNT_TIME, "测试token接口"), //
		};
		for (Api api : apis) {
			apiMap.put(api.getApi(), api);
		}
	}
}
