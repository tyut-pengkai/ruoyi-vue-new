package com.ruoyi.agent.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.agent.domain.vo.TemplateInfoVo;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.TemplateType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理卡类关联对象 sys_agent_item
 *
 * @author zwgu
 * @date 2022-06-11
 */
public class SysAgentItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 代理ID
     */
    @Excel(name = "代理ID")
    private Long agentId;

    /**
     * 卡类别1充值卡2单码
     */
    @Excel(name = "卡类别1充值卡2单码")
    private TemplateType templateType;

    /**
     * 卡类ID
     */
    @Excel(name = "卡类ID")
    private Long templateId;

    /**
     * 价格
     */
    @Excel(name = "零售价格")
    private BigDecimal price;

    /**
     * 价格
     */
    @Excel(name = "代理价格")
    private BigDecimal agentPrice;

    /**
     * 代理该卡过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "代理该卡过期时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    /**
     * 所属账号信息
     */
    @Excels({
            @Excel(name = "用户账号", targetAttr = "userName", type = Excel.Type.EXPORT),
            @Excel(name = "用户昵称", targetAttr = "nickName", type = Excel.Type.EXPORT)
    })
    private SysUser user;

    private TemplateInfoVo templateInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public TemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public BigDecimal getAgentPrice() {
        return agentPrice;
    }

    public void setAgentPrice(BigDecimal agentPrice) {
        this.agentPrice = agentPrice;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

    public TemplateInfoVo getTemplateInfo() {
        return templateInfo;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setTemplateInfo(TemplateInfoVo templateInfo) {
        this.templateInfo = templateInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("agentId", getAgentId())
                .append("templateType", getTemplateType())
                .append("templateId", getTemplateId())
                .append("price", getPrice())
                .append("agentPrice", getAgentPrice())
                .append("expireTime", getExpireTime())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
