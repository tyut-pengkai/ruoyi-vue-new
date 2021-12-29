package com.ruoyi.api.v1.constants;

import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.BillType;

public class Constants {
    // 用于校验接口调用是否符合软件设定
    public static AuthType[] AUTH_TYPE_ALL = new AuthType[]{AuthType.ACCOUNT, AuthType.LOGIN_CODE};
    public static BillType[] BILL_TYPE_ALL = new BillType[]{BillType.TIME, BillType.POINT};
    // 用于API文档接口分类
    public static String API_TAG_ACCOUNT_TIME = "账号-计时模式";
    public static String API_TAG_ACCOUNT_POINT = "账号-计点模式";
    public static String API_TAG_CODE_TIME = "登录码-计时模式";
    public static String API_TAG_CODE_POINT = "登录码-计点模式";
    public static String API_TAG_COMMON = "所有模式通用";
    public static String[] API_TAG_ACCOUNT = new String[]{API_TAG_ACCOUNT_TIME, API_TAG_ACCOUNT_POINT};
    public static String[] API_TAG_CODE = new String[]{API_TAG_CODE_TIME, API_TAG_CODE_POINT};
    public static String[] API_TAG_TIME = new String[]{API_TAG_ACCOUNT_TIME, API_TAG_CODE_TIME};
    public static String[] API_TAG_POINT = new String[]{API_TAG_ACCOUNT_POINT, API_TAG_CODE_POINT};
    public static String[] API_TAG_ALL = new String[]{API_TAG_ACCOUNT_TIME, API_TAG_ACCOUNT_POINT, API_TAG_CODE_TIME,
            API_TAG_CODE_POINT, API_TAG_COMMON};

}
