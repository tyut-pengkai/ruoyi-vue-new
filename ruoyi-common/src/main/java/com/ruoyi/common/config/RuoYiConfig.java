package com.ruoyi.common.config;

import com.ruoyi.common.utils.PathUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 读取项目相关配置
 *
 * @author ruoyi
 */
@Component
@ConfigurationProperties(prefix = "ruoyi")
public class RuoYiConfig {
    /**
     * 项目名称
     */
    private String name;
    private String shortName;

    /**
     * 版本
     */
    private String version;

    private Long versionNo;

    private String dbVersion;

    private Long dbVersionNo;

    private String url;

    private String email;

    /**
     * 版权年份
     */
    private String copyrightYear;

    private String copyright;
//    /**
//     * 实例演示开关
//     */
//    private boolean demoEnabled;

//    /**
//     * 上传路径
//     */
//    private static String profile;

    /**
     * 获取地址开关
     */
    private static boolean addressEnabled;

    /**
     * 验证码类型
     */
    private static String captchaType;

    /**
     * 是否为授权站，授权站前端商城将显示获取授权选项卡
     */
    private boolean isLicenseServer;

    private boolean enableFrontEnd;

//    public boolean isLicenseServer() {
//        return isLicenseServer;
//    }
//
//    public void setLicenseServer(boolean licenseServer) {
//        isLicenseServer = licenseServer;
//    }

    public String getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(String dbVersion) {
        this.dbVersion = dbVersion;
    }

    public Long getDbVersionNo() {
        return dbVersionNo;
    }

    public void setDbVersionNo(Long dbVersionNo) {
        this.dbVersionNo = dbVersionNo;
    }

    public boolean isEnableFrontEnd() {
        return enableFrontEnd;
    }

    public void setEnableFrontEnd(boolean enableFrontEnd) {
        this.enableFrontEnd = enableFrontEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static String getProfile() {
        return PathUtils.getUserPath() + File.separator + "uploadPath";
    }

//    public static String getProfile() {
//        return profile;
//    }
//
//    public void setProfile(String profile) {
//        RuoYiConfig.profile = profile;
//    }

//    public boolean isDemoEnabled()
//    {
//        return demoEnabled;
//    }
//
//    public void setDemoEnabled(boolean demoEnabled)
//    {
//        this.demoEnabled = demoEnabled;
//    }

    public String getCopyrightYear() {
        return copyrightYear;
    }

    public void setCopyrightYear(String copyrightYear) {
        this.copyrightYear = copyrightYear;
    }

    public static boolean isAddressEnabled() {
        return addressEnabled;
    }

    public void setAddressEnabled(boolean addressEnabled) {
        RuoYiConfig.addressEnabled = addressEnabled;
    }

    public static String getCaptchaType() {
        return captchaType;
    }

    public void setCaptchaType(String captchaType) {
        RuoYiConfig.captchaType = captchaType;
    }

    /**
     * 获取导入上传路径
     */
    public static String getImportPath()
    {
        return getProfile() + "/import";
    }

    /**
     * 获取头像上传路径
     */
    public static String getAvatarPath()
    {
        return getProfile() + "/avatar";
    }

    /**
     * 获取下载路径
     */
    public static String getDownloadPath() {
        return getProfile() + "/download/";
    }

    /**
     * 获取上传路径
     */
    public static String getUploadPath() {
        return getProfile() + "/upload";
    }

    /**
     * 获取api上传路径
     */
    public static String getGlobalFilePath() {
        return getProfile() + "/global";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public Long getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Long versionNo) {
        this.versionNo = versionNo;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public boolean isIsLicenseServer() {
        return isLicenseServer;
    }

    public void setIsLicenseServer(boolean isLicenseServer) {
        this.isLicenseServer = isLicenseServer;
    }
}
