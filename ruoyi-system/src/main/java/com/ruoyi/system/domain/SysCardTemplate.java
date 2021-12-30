package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.enums.ChargeRule;
import com.ruoyi.common.enums.GenRule;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 卡密模板对象 sys_card_template
 *
 * @author zwgu
 * @date 2021-11-28
 */
public class SysCardTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 模板ID */
    private Long templateId;

    /** 软件ID */
    @Excel(name = "软件ID")
    private Long appId;

    /** 卡名称 */
    @Excel(name = "卡名称")
    private String cardName;

    /** 卡号前缀 */
    private String cardNoPrefix;

    /** 卡号后缀 */
    private String cardNoSuffix;

    /** 卡描述 */
    @Excel(name = "卡描述")
    private String cardDescription;

    /** 额度 */
    @Excel(name = "额度")
    private Long quota;

    /** 价格 */
    @Excel(name = "价格")
    private BigDecimal price;

    /** 卡号长度 */
    @Excel(name = "卡号长度")
    private Integer cardNoLen;

    /** 卡号生成规则 */
    @Excel(name = "卡号生成规则")
    private GenRule cardNoGenRule;

    /** 卡号正则 */
    @Excel(name = "卡号正则")
    private String cardNoRegex;

    /** 密码长度 */
    @Excel(name = "密码长度")
    private Integer cardPassLen;

    /** 密码生成规则 */
    @Excel(name = "密码生成规则")
    private GenRule cardPassGenRule;

    /** 密码正则 */
    @Excel(name = "密码正则")
    private String cardPassRegex;

    /** 充值规则 */
    @Excel(name = "充值规则")
    private ChargeRule chargeRule;

    /** 是否上架 */
    @Excel(name = "是否上架")
    private String onSale;

    /** 优先库存 */
    @Excel(name = "优先库存")
    private String firstStock;

    /** 有效时长 */
    @Excel(name = "有效时长")
    private Long effectiveDuration;

    /** 模板状态 */
    @Excel(name = "模板状态")
    private String status;

    /** 允许自动生成 */
    @Excel(name = "允许自动生成")
    private String enableAutoGen;

    /**
     * 所属软件信息
     */
    @Excel(name = "软件名称", targetAttr = "appName", type = Excel.Type.EXPORT)
    private SysApp app;

    public SysApp getApp() {
        return app;
    }

    public void setApp(SysApp app) {
        this.app = app;
    }

    public void setTemplateId(Long templateId) 
    {
        this.templateId = templateId;
    }

    public Long getTemplateId() 
    {
        return templateId;
    }
    public void setAppId(Long appId) 
    {
        this.appId = appId;
    }

    public Long getAppId() 
    {
        return appId;
    }
    public void setCardName(String cardName) 
    {
        this.cardName = cardName;
    }

    public String getCardName() 
    {
        return cardName;
    }
    public void setCardNoPrefix(String cardNoPrefix) 
    {
        this.cardNoPrefix = cardNoPrefix;
    }

    public String getCardNoPrefix() 
    {
        return cardNoPrefix;
    }
    public void setCardNoSuffix(String cardNoSuffix) 
    {
        this.cardNoSuffix = cardNoSuffix;
    }

    public String getCardNoSuffix() 
    {
        return cardNoSuffix;
    }
    public void setCardDescription(String cardDescription) 
    {
        this.cardDescription = cardDescription;
    }

    public String getCardDescription() 
    {
        return cardDescription;
    }
    public void setQuota(Long quota) 
    {
        this.quota = quota;
    }

    public Long getQuota() 
    {
        return quota;
    }
    public void setPrice(BigDecimal price) 
    {
        this.price = price;
    }

    public BigDecimal getPrice() 
    {
        return price;
    }
    public void setCardNoLen(Integer cardNoLen)
    {
        this.cardNoLen = cardNoLen;
    }

    public Integer getCardNoLen()
    {
        return cardNoLen;
    }
    public void setCardNoGenRule(GenRule cardNoGenRule)
    {
        this.cardNoGenRule = cardNoGenRule;
    }

    public GenRule getCardNoGenRule()
    {
        return cardNoGenRule;
    }
    public void setCardNoRegex(String cardNoRegex) 
    {
        this.cardNoRegex = cardNoRegex;
    }

    public String getCardNoRegex() 
    {
        return cardNoRegex;
    }
    public void setCardPassLen(Integer cardPassLen)
    {
        this.cardPassLen = cardPassLen;
    }

    public Integer getCardPassLen()
    {
        return cardPassLen;
    }
    public void setCardPassGenRule(GenRule cardPassGenRule)
    {
        this.cardPassGenRule = cardPassGenRule;
    }

    public GenRule getCardPassGenRule()
    {
        return cardPassGenRule;
    }
    public void setCardPassRegex(String cardPassRegex) 
    {
        this.cardPassRegex = cardPassRegex;
    }

    public String getCardPassRegex() 
    {
        return cardPassRegex;
    }
    public void setChargeRule(ChargeRule chargeRule)
    {
        this.chargeRule = chargeRule;
    }

    public ChargeRule getChargeRule()
    {
        return chargeRule;
    }
    public void setOnSale(String onSale) 
    {
        this.onSale = onSale;
    }

    public String getOnSale() 
    {
        return onSale;
    }
    public void setFirstStock(String firstStock) 
    {
        this.firstStock = firstStock;
    }

    public String getFirstStock() 
    {
        return firstStock;
    }
    public void setEffectiveDuration(Long effectiveDuration) 
    {
        this.effectiveDuration = effectiveDuration;
    }

    public Long getEffectiveDuration() 
    {
        return effectiveDuration;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setEnableAutoGen(String enableAutoGen) 
    {
        this.enableAutoGen = enableAutoGen;
    }

    public String getEnableAutoGen() 
    {
        return enableAutoGen;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("templateId", getTemplateId())
            .append("appId", getAppId())
            .append("cardName", getCardName())
            .append("cardNoPrefix", getCardNoPrefix())
            .append("cardNoSuffix", getCardNoSuffix())
            .append("cardDescription", getCardDescription())
            .append("quota", getQuota())
            .append("price", getPrice())
            .append("cardNoLen", getCardNoLen())
            .append("cardNoGenRule", getCardNoGenRule())
            .append("cardNoRegex", getCardNoRegex())
            .append("cardPassLen", getCardPassLen())
            .append("cardPassGenRule", getCardPassGenRule())
            .append("cardPassRegex", getCardPassRegex())
            .append("chargeRule", getChargeRule())
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