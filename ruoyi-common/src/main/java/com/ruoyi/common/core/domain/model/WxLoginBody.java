package com.ruoyi.common.core.domain.model;

/**
 * 微信用户登录对象
 *
 * @author ruoyi
 */
public class WxLoginBody
{
    /**
     * 临时登陆凭证 code 只能使用一次
     */
    private String code;

    /**
     * 用户信息加密数据
     */
    private String encryptedData;

    /**
     * 用户信息iv
     */
    private String iv;

    /**
     * 原始数据
     */
    private String rawData;

    /**
     * 签名
     */
    private String signature;

    /**
     * 手机号加密数据
     */
    private String phoneEncryptedData;

    /**
     * 手机号iv
     */
    private String phoneIv;
    
    /**
     * 是否包含手机号信息
     */
    private Boolean hasPhoneInfo;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPhoneEncryptedData() {
        return phoneEncryptedData;
    }

    public void setPhoneEncryptedData(String phoneEncryptedData) {
        this.phoneEncryptedData = phoneEncryptedData;
    }

    public String getPhoneIv() {
        return phoneIv;
    }

    public void setPhoneIv(String phoneIv) {
        this.phoneIv = phoneIv;
    }
    
    public Boolean getHasPhoneInfo() {
        return hasPhoneInfo;
    }

    public void setHasPhoneInfo(Boolean hasPhoneInfo) {
        this.hasPhoneInfo = hasPhoneInfo;
    }
}