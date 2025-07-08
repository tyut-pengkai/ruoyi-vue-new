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
    
    /**
     * 微信openId
     */
    private String openId;
    
    /**
     * 用户信息对象（昵称、头像、性别等）
     */
    private WxUserInfo userInfo;

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
    
    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
    
    public WxUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(WxUserInfo userInfo) {
        this.userInfo = userInfo;
    }
    
    /**
     * 微信用户信息对象
     */
    public static class WxUserInfo {
        private String nickName;
        private String avatarUrl;
        private String gender;
        private String country;
        private String province;
        private String city;
        private String language;
        
        public String getNickName() {
            return nickName;
        }
        
        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
        
        public String getAvatarUrl() {
            return avatarUrl;
        }
        
        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }
        
        public String getGender() {
            return gender;
        }
        
        public void setGender(String gender) {
            this.gender = gender;
        }
        
        public String getCountry() {
            return country;
        }
        
        public void setCountry(String country) {
            this.country = country;
        }
        
        public String getProvince() {
            return province;
        }
        
        public void setProvince(String province) {
            this.province = province;
        }
        
        public String getCity() {
            return city;
        }
        
        public void setCity(String city) {
            this.city = city;
        }
        
        public String getLanguage() {
            return language;
        }
        
        public void setLanguage(String language) {
            this.language = language;
        }
    }
}