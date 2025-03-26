package com.ruoyi.xkt.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 档口对象 store
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Store extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口ID
     */
    private Long storeId;

    /**
     * 档口负责人ID
     */
    @Excel(name = "档口负责人ID")
    private Long userId;

    /**
     * 档口名称
     */
    @Excel(name = "档口名称")
    private String storeName;

    /**
     * 品牌名称
     */
    @Excel(name = "品牌名称")
    private String brandName;

    /**
     * 登录账号
     */
    @Excel(name = "登录账号")
    private String loginAccount;

    /**
     * 联系人
     */
    @Excel(name = "联系人")
    private String contactName;

    /**
     * 联系电话
     */
    @Excel(name = "联系电话")
    private String contactPhone;

    /**
     * 备选联系电话
     */
    @Excel(name = "备选联系电话")
    private String contactBackPhone;

    /**
     * 微信账号
     */
    @Excel(name = "微信账号")
    private String wechatAccount;

    /**
     * QQ账号
     */
    @Excel(name = "QQ账号")
    private String qqAccount;

    /**
     * 支付宝账号
     */
    @Excel(name = "支付宝账号")
    private String alipayAccount;

    /**
     * 经营年限
     */
    @Excel(name = "经营年限")
    private Integer operateYears;

    /**
     * 档口地址
     */
    @Excel(name = "档口地址")
    private String storeAddress;

    /**
     * 工厂地址
     */
    @Excel(name = "工厂地址")
    private String facAddress;

    /**
     * 生产规模
     */
    @Excel(name = "生产规模")
    private Long prodScale;

    /**
     * 保证金
     */
    @Excel(name = "保证金")
    private BigDecimal integrityGold;

    /**
     * 试用截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "试用截止时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date trialEndTime;

    /**
     * 已使用文件大小
     */
    @Excel(name = "已使用文件大小")
    private BigDecimal storageUsage;

    /**
     * 档口状态
     */
    @Excel(name = "档口状态")
    private Long storeStatus;

    /**
     * 档口模板ID
     */
    @Excel(name = "档口模板ID")
    private Integer templateNum;

    /**
     * 版本号
     */
    @Excel(name = "版本号")
    private Long version;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("storeId", getStoreId())
                .append("userId", getUserId())
                .append("storeName", getStoreName())
                .append("brandName", getBrandName())
                .append("loginAccount", getLoginAccount())
                .append("contactName", getContactName())
                .append("contactPhone", getContactPhone())
                .append("contactBackPhone", getContactBackPhone())
                .append("wechatAccount", getWechatAccount())
                .append("qqAccount", getQqAccount())
                .append("alipayAccount", getAlipayAccount())
                .append("operateYears", getOperateYears())
                .append("storeAddress", getStoreAddress())
                .append("facAddress", getFacAddress())
                .append("prodScale", getProdScale())
                .append("integrityGold", getIntegrityGold())
                .append("remark", getRemark())
                .append("trialEndTime", getTrialEndTime())
                .append("storageUsage", getStorageUsage())
                .append("storeStatus", getStoreStatus())
                .append("templateNum", getTemplateNum())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
