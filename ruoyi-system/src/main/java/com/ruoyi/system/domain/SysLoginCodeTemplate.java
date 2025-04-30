package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.enums.GenRule;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 单码类别对象 sys_login_code_template
 *
 * @author zwgu
 * @date 2022-01-06
 */
public class SysLoginCodeTemplate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 卡类ID
     */
    private Long templateId;

    /**
     * 软件ID
     */
    @Excel(name = "软件ID")
    private Long appId;

    /**
     * 卡名称
     */
    @Excel(name = "单码名称")
    private String cardName;

    /**
     * 单码前缀
     */
    @Excel(name = "单码前缀")
    private String cardNoPrefix;

    /**
     * 单码后缀
     */
    @Excel(name = "单码后缀")
    private String cardNoSuffix;

    /**
     * 卡描述
     */
    @Excel(name = "卡描述")
    private String cardDescription;

    /**
     * 额度
     */
    @Excel(name = "额度")
    private Long quota;

    /**
     * 价格
     */
    @Excel(name = "价格")
    private BigDecimal price;

    /**
     * 单码长度
     */
    @Excel(name = "单码长度")
    private Integer cardNoLen;

    /**
     * 单码生成规则
     */
    @Excel(name = "单码生成规则")
    private GenRule cardNoGenRule;

    /**
     * 单码正则
     */
    @Excel(name = "单码正则")
    private String cardNoRegex;

    /**
     * 是否上架
     */
    @Excel(name = "是否上架")
    private String onSale;

    /**
     * 优先库存
     */
    @Excel(name = "优先库存")
    private String firstStock;

    /**
     * 有效时长
     */
    @Excel(name = "有效时长")
    private Long effectiveDuration;

    /**
     * 卡类状态
     */
    @Excel(name = "单码类别状态")
    private String status;

    /**
     * 允许自动生成
     */
    @Excel(name = "允许自动生成")
    private String enableAutoGen;

    /**
     * 价格
     */
    @Excel(name = "代理价格")
    private BigDecimal agentPrice;

    /**
     * 所属软件信息
     */
    @Excel(name = "软件名称", targetAttr = "appName", type = Excel.Type.EXPORT)
    private SysApp app;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 登录用户数量限制，整数，-1为不限制，默认为-1
     */
    @Excel(name = "由卡密继承来的登录用户数量限制，整数，-1为不限制，-2为不生效，默认为-2")
    private Integer cardLoginLimitU;

    /**
     * 登录机器数量限制，整数，-1为不限制，默认为-1
     */
    @Excel(name = "由卡密继承来的登录机器数量限制，整数，-1为不限制，-2为不生效，默认为-2")
    private Integer cardLoginLimitM;

    /**
     * 卡密自定义参数
     */
    @Excel(name = "卡密自定义参数")
    private String cardCustomParams;

    /**
     * 购卡URL
     */
    @Excel(name = "购卡URL")
    private String shopUrl;

    /**
     * 允许解绑
     */
    @Excel(name = "允许解绑")
    private String enableUnbind;

    @Excel(name = "最少购买数量")
    private Integer minBuyNum;

    @Excel(name = "允许换卡")
    private String enableReplace;

    @Excel(name = "换卡至少剩余额度，0不限制 -1未使用")
    private Long replaceThreshold;

    @Excel(name = "排序")
    private Integer sort;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getEnableReplace() {
        return enableReplace;
    }

    public void setEnableReplace(String enableReplace) {
        this.enableReplace = enableReplace;
    }

    public Long getReplaceThreshold() {
        return replaceThreshold;
    }

    public void setReplaceThreshold(Long replaceThreshold) {
        this.replaceThreshold = replaceThreshold;
    }

    public Integer getMinBuyNum() {
        return minBuyNum;
    }

    public void setMinBuyNum(Integer minBuyNum) {
        this.minBuyNum = minBuyNum;
    }

    public String getEnableUnbind() {
        return enableUnbind;
    }

    public void setEnableUnbind(String enableUnbind) {
        this.enableUnbind = enableUnbind;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public Integer getCardLoginLimitU() {
        return cardLoginLimitU;
    }

    public void setCardLoginLimitU(Integer cardLoginLimitU) {
        this.cardLoginLimitU = cardLoginLimitU;
    }

    public Integer getCardLoginLimitM() {
        return cardLoginLimitM;
    }

    public void setCardLoginLimitM(Integer cardLoginLimitM) {
        this.cardLoginLimitM = cardLoginLimitM;
    }

    public String getCardCustomParams() {
        return cardCustomParams;
    }

    public void setCardCustomParams(String cardCustomParams) {
        this.cardCustomParams = cardCustomParams;
    }

    public SysApp getApp() {
        return app;
    }

    public void setApp(SysApp app) {
        this.app = app;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNoPrefix() {
        return cardNoPrefix;
    }

    public void setCardNoPrefix(String cardNoPrefix) {
        this.cardNoPrefix = cardNoPrefix;
    }

    public String getCardNoSuffix() {
        return cardNoSuffix;
    }

    public void setCardNoSuffix(String cardNoSuffix) {
        this.cardNoSuffix = cardNoSuffix;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public Long getQuota() {
        return quota;
    }

    public void setQuota(Long quota) {
        this.quota = quota;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCardNoLen() {
        return cardNoLen;
    }

    public void setCardNoLen(Integer cardNoLen) {
        this.cardNoLen = cardNoLen;
    }

    public GenRule getCardNoGenRule() {
        return cardNoGenRule;
    }

    public void setCardNoGenRule(GenRule cardNoGenRule) {
        this.cardNoGenRule = cardNoGenRule;
    }

    public String getCardNoRegex() {
        return cardNoRegex;
    }

    public void setCardNoRegex(String cardNoRegex) {
        this.cardNoRegex = cardNoRegex;
    }

    public String getOnSale() {
        return onSale;
    }

    public void setOnSale(String onSale) {
        this.onSale = onSale;
    }

    public String getFirstStock() {
        return firstStock;
    }

    public void setFirstStock(String firstStock) {
        this.firstStock = firstStock;
    }

    public Long getEffectiveDuration() {
        return effectiveDuration;
    }

    public void setEffectiveDuration(Long effectiveDuration) {
        this.effectiveDuration = effectiveDuration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnableAutoGen() {
        return enableAutoGen;
    }

    public void setEnableAutoGen(String enableAutoGen) {
        this.enableAutoGen = enableAutoGen;
    }

    public BigDecimal getAgentPrice() {
        return agentPrice;
    }

    public void setAgentPrice(BigDecimal agentPrice) {
        this.agentPrice = agentPrice;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("templateId", getTemplateId())
                .append("appId", getAppId())
                .append("cardName", getCardName())
                .append("cardNoPrefix", getCardNoPrefix())
                .append("cardNoSuffix", getCardNoSuffix())
                .append("cardDescription", getCardDescription())
                .append("quota", getQuota())
                .append("price", getPrice())
                .append("agentPrice", getAgentPrice())
                .append("cardNoLen", getCardNoLen())
                .append("cardNoGenRule", getCardNoGenRule())
                .append("cardNoRegex", getCardNoRegex())
                .append("onSale", getOnSale())
                .append("firstStock", getFirstStock())
                .append("effectiveDuration", getEffectiveDuration())
                .append("status", getStatus())
                .append("enableAutoGen", getEnableAutoGen())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
