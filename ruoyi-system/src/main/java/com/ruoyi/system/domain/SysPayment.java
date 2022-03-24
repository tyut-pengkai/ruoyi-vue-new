package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 支付配置对象 sys_payment
 *
 * @author zwgu
 * @date 2022-03-24
 */
public class SysPayment extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 支付ID
     */
    private Long payId;

    /**
     * 支付编码
     */
    @Excel(name = "支付编码")
    private String code;

    /**
     * 支付名称
     */
    @Excel(name = "支付名称")
    private String name;

    /**
     * 描述
     */
    @Excel(name = "描述")
    private String description;

    /**
     * 移动端
     */
    @Excel(name = "移动端")
    private String mobile;

    /**
     * 电脑端
     */
    @Excel(name = "电脑端")
    private String pc;

    /**
     * 图标
     */
    @Excel(name = "图标")
    private String icon;

    /**
     * 配置
     */
    @Excel(name = "配置")
    private String config;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String status;

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("payId", getPayId())
                .append("code", getCode())
                .append("name", getName())
                .append("description", getDescription())
                .append("mobile", getMobile())
                .append("pc", getPc())
                .append("icon", getIcon())
                .append("config", getConfig())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
