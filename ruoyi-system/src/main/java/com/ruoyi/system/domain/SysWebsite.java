package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 网站配置 sys_website
 *
 * @author zwgu
 * @date 2022-03-22
 */
public class SysWebsite extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * favicon
     */
    @Excel(name = "favicon")
    private String favicon;

    /**
     * logo
     */
    @Excel(name = "logo")
    private String logo;

    /**
     * 网站名称
     */
    @Excel(name = "网站名称")
    private String name;

    /**
     * 网站短名称
     */
    @Excel(name = "网站短名称")
    private String shortName;

    /**
     * 网站短名称
     */
    @Excel(name = "商城名称")
    private String shopName;

    /**
     * 网站域名
     */
    @Excel(name = "网站域名")
    private String domain;

    /**
     * 联系方式
     */
    @Excel(name = "联系方式")
    private String contact;

    /**
     * 关键字
     */
    @Excel(name = "关键字")
    private String keywords;

    /**
     * 网站描述
     */
    @Excel(name = "网站描述")
    private String description;

    /**
     * 网站状态
     */
    @Excel(name = "网站状态")
    private String status;

    /**
     * 用户自定义后台登录入口
     */
//    @JSONField(serialize = false)
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String safeEntrance;

    @TableField(exist = false)
    private String pageSize;

    @TableField(exist = false)
    private char isSafeEntrance;

    /**
     * 是否开启前台页面
     */
    private String enableFrontEnd;
    /**
     * ICP备案
     */
    @Excel(name = "备案号")
    private String icp;

    public String getEnableFrontEnd() {
        return enableFrontEnd;
    }

    public void setEnableFrontEnd(String enableFrontEnd) {
        this.enableFrontEnd = enableFrontEnd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSafeEntrance() {
        return safeEntrance;
    }

    public void setSafeEntrance(String safeEntrance) {
        this.safeEntrance = safeEntrance;
    }

    public char getIsSafeEntrance() {
        return isSafeEntrance;
    }

    public void setIsSafeEntrance(char isSafeEntrance) {
        this.isSafeEntrance = isSafeEntrance;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getIcp() {
        return icp;
    }

    public void setIcp(String icp) {
        this.icp = icp;
    }

    @Override
    public String toString() {
        return "SysWebsite{" +
                "id=" + id +
                ", favicon='" + favicon + '\'' +
                ", logo='" + logo + '\'' +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", shopName='" + shopName + '\'' +
                ", domain='" + domain + '\'' +
                ", contact='" + contact + '\'' +
                ", keywords='" + keywords + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", safeEntrance='" + safeEntrance + '\'' +
                ", isSafeEntrance=" + isSafeEntrance +
                ", icp=" + icp +
                '}';
    }
}
