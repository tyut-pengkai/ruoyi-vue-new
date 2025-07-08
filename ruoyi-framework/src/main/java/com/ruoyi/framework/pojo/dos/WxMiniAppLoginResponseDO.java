package com.ruoyi.framework.pojo.dos;
 
import lombok.Data;
 
/**
 * 微信小程序登录请求响应DO
 * @author cmc
 */
@Data
public class WxMiniAppLoginResponseDO {
 
    private String openid;
 
    private String sessionKey;
 
    private String unionid;
 
    private String errcode;
 
    private String errmsg;
}