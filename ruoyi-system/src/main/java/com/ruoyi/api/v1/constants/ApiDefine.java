package com.ruoyi.api.v1.constants;

import com.alibaba.fastjson.JSON;
import com.ruoyi.api.v1.domain.Api;
import com.ruoyi.api.v1.domain.Function;
import com.ruoyi.api.v1.domain.Param;
import com.ruoyi.api.v1.domain.Resp;
import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.common.enums.AuthType;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ApiDefine {

    public static String BASE_PACKAGE = "com.ruoyi.api.v1.api";
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
    public static Map<String, Function> functionMap = new HashMap<>();

    static {
        publicParamsAuth.add(new Param("api", true, "请求的API接口，此处为${api}", "${api}"));
        publicParamsAuth.add(new Param("appSecret", true, "AppSecret"));
        publicParamsAuth.add(new Param("vstr", false, "用作标记或验证的冗余数据，将原样返回"));
        publicParamsAuth.add(new Param("timestamp", true, "13位时间戳（精确到毫秒）"));
        publicParamsAuth.add(new Param("sign", true, "数据签名"));
        // ===================================
        publicParamsNoAuth.add(new Param("api", true, "请求的API接口，此处为${api}", "${api}"));
        publicParamsNoAuth.add(new Param("appSecret", true, "AppSecret"));
        publicParamsNoAuth.add(new Param("sign", true, "数据签名"));
    }

    static {
        List<Class<?>> classList = MyUtils.getClassesFromPackage(BASE_PACKAGE);
        Api[] apis = new Api[]{ //
                // 调试工具
                new Api("calcSign", "计算SIGN值", false, Constants.API_TAG_DEV_TOOL, "计算SIGN值", Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL), //
                // noAuth
                new Api("login.nu", "账号登录", false, Constants.API_TAG_ACCOUNT, "账号登录接口", new AuthType[]{AuthType.ACCOUNT}, Constants.BILL_TYPE_ALL,
                        new Param[]{
                                new Param("username", true, "账号"), //
                                new Param("password", true, "密码"), //
                                new Param("appVer", true, "软件版本号"),
                                new Param("deviceCode", false, "设备码，如果开启设备绑定，则必须提供"),
                                new Param("md5", false, "软件MD5"),
                                new Param("autoReducePoint", false, "计点模式下是否登录成功后自动扣除用户点数（扣除1），默认值为true")
                        },
                        new Resp(Resp.DataType.string, "登录成功返回token")), //
                new Api("login.nc", "单码登录", false, Constants.API_TAG_CODE, "单码登录接口", new AuthType[]{AuthType.LOGIN_CODE}, Constants.BILL_TYPE_ALL,
                        new Param[]{
                                new Param("loginCode", true, "单码"), //
                                new Param("appVer", true, "软件版本号"),
                                new Param("deviceCode", false, "设备码，如果开启设备绑定，则必须提供"),
                                new Param("md5", false, "软件MD5"),
                                new Param("autoReducePoint", false, "计点模式下是否登录成功后自动扣除用户点数（扣除1），默认值为true")
                        },
                        new Resp(Resp.DataType.string, "登录成功返回token")),
                new Api("trialLogin.ng", "试用登录", false, Constants.API_TAG_GENERAL, "试用登录接口", Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL,
                        new Param[]{
                                new Param("appVer", true, "软件版本号"),
                                new Param("deviceCode", false, "设备码，如果开启设备绑定，则必须提供"),
                                new Param("md5", false, "软件MD5")
                        },
                        new Resp(Resp.DataType.string, "登录成功返回token")), //
                // Auth
                new Api("logout.ag", "注销登录", true, Constants.API_TAG_GENERAL, "注销登录接口",
                        Constants.AUTH_TYPE_ALL, Constants.BILL_TYPE_ALL, null,
                        new Resp(Resp.DataType.string, "成功返回0")), //
        };
        for (Api api : apis) {
            apiMap.put(api.getApi(), api);
        }
        for (Class<?> clazz : classList) {
            try {
                Constructor<?> ct = clazz.getDeclaredConstructor();
                Object obj = ct.newInstance();
                Function func = (Function) obj;
                Api api = func.getApi();
                apiMap.put(api.getApi(), api);
                functionMap.put(api.getApi(), func);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        log.info("总共加载API接口 {} 个，详情如下：", apiMap.size());
        log.info(JSON.toJSONString(apiMap.keySet()));
        log.debug(JSON.toJSONString(apiMap));
    }
}
