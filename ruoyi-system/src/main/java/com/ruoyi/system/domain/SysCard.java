package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.ChargeRule;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 卡密对象 sys_card
 *
 * @author zwgu
 * @date 2021-12-03
 */
public class SysCard extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 卡密ID
     */
    private Long cardId;

    /**
     * 软件ID
     */
    private Long appId;

    /**
     * 卡名称
     */
    @Excel(name = "卡名称", sort = 2)
    private String cardName;

    /**
     * 卡号
     */
    @Excel(name = "卡号", sort = 3)
    private String cardNo;

    /**
     * 密码
     */
    @Excel(name = "密码", sort = 4)
    private String cardPass;

    /**
     * 额度
     */
    @Excel(name = "额度", prompt = "单位秒（计时模式）或点（计点模式）", type = Excel.Type.EXPORT)
    private Long quota;

    /**
     * 价格
     */
    @Excel(name = "零售价格", type = Excel.Type.EXPORT)
    private BigDecimal price;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "充值过期时间", prompt = "卡允许激活的期限，用户需要在此日期前激活卡密，超过此日期此卡密将无法使用", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private Date expireTime;

    /**
     * 是否售出
     */
    @Excel(name = "是否售出", dictType = "sys_yes_no", sort = 6)
    private String isSold;

    /**
     * 是否上架
     */
    @Excel(name = "是否上架", dictType = "sys_yes_no", sort = 7)
    private String onSale;

    /**
     * 是否被充值
     */
    @Excel(name = "是否被充值", dictType = "sys_yes_no", sort = 8)
    private String isCharged;

    /**
     * 充值时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "充值时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", sort = 9)
    private Date chargeTime;

    /**
     * 卡类ID
     */
    private Long templateId;

    /**
     * 卡密状态
     */
    @Excel(name = "卡密状态", dictType = "sys_normal_disable", sort = 5)
    private String status;

    /**
     * 充值规则
     */
    @Excel(name = "充值规则", dictType = "sys_charge_rule", type = Excel.Type.EXPORT)
    private ChargeRule chargeRule;

    /**
     * 是否代理制卡
     */
    @Excel(name = "是否代理制卡", dictType = "sys_yes_no", type = Excel.Type.EXPORT)
    private String isAgent;

    /**
     * 所属软件信息
     */
    @Excel(name = "软件名称", targetAttr = "appName", sort = 1)
    private SysApp app = new SysApp();

    /**
     * 批量制卡数量
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer genQuantity;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 登录用户数量限制，整数，-1为不限制，默认为-1
     */
    @Excel(name = "登录用户数量限制(卡)", prompt = "由卡密继承来的登录用户数量限制，整数，-1为不限制，-2为不生效，默认为-2", type = Excel.Type.EXPORT)
    private Integer cardLoginLimitU;

    /**
     * 登录机器数量限制，整数，-1为不限制，默认为-1
     */
    @Excel(name = "登录机器数量限制(卡)", prompt = "由卡密继承来的登录机器数量限制，整数，-1为不限制，-2为不生效，默认为-2", type = Excel.Type.EXPORT)
    private Integer cardLoginLimitM;

    /**
     * 卡密自定义参数
     */
    @Excel(name = "卡密自定义参数", type = Excel.Type.EXPORT)
    private String cardCustomParams;

    /**
     * 制卡批次号
     */
    @Excel(name = "制卡批次号", sort = 10)
    private String batchNo;

    private Long agentId;

    @Excel(name = "所属代理账号", targetAttr = "userName", sort = 11)
    private SysUser agentUser = new SysUser();

    public SysUser getAgentUser() {
        return agentUser;
    }

    public void setAgentUser(SysUser agentUser) {
        this.agentUser = agentUser;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public Integer getGenQuantity() {
        return genQuantity;
    }

    public void setGenQuantity(Integer genQuantity) {
        this.genQuantity = genQuantity;
    }

    public SysApp getApp() {
        return app;
    }

    public void setApp(SysApp app) {
        this.app = app;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardPass() {
        return cardPass;
    }

    public void setCardPass(String cardPass) {
        this.cardPass = cardPass;
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

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public String getIsSold() {
        return isSold;
    }

    public void setIsSold(String isSold) {
        this.isSold = isSold;
    }

    public String getOnSale() {
        return onSale;
    }

    public void setOnSale(String onSale) {
        this.onSale = onSale;
    }

    public String getIsCharged() {
        return isCharged;
    }

    public void setIsCharged(String isCharged) {
        this.isCharged = isCharged;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ChargeRule getChargeRule() {
        return chargeRule;
    }

    public void setChargeRule(ChargeRule chargeRule) {
        this.chargeRule = chargeRule;
    }

    public Date getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(Date chargeTime) {
        this.chargeTime = chargeTime;
    }

    public String getIsAgent() {
        return isAgent;
    }

    public void setIsAgent(String isAgent) {
        this.isAgent = isAgent;
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
                .append("cardId", getCardId())
                .append("appId", getAppId())
                .append("cardName", getCardName())
                .append("cardNo", getCardNo())
                .append("cardPass", getCardPass())
                .append("quota", getQuota())
                .append("price", getPrice())
                .append("expireTime", getExpireTime())
                .append("isSold", getIsSold())
                .append("onSale", getOnSale())
                .append("isCharged", getIsCharged())
                .append("chargeTime", getChargeTime())
                .append("templateId", getTemplateId())
                .append("status", getStatus())
                .append("chargeRule", getChargeRule())
                .append("isAgent", getIsAgent())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
