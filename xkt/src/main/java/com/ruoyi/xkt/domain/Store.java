package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class Store extends XktBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 档口ID
     */
    @TableId
    private Long id;

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
     * 档口LOGO
     */
    private Long storeLogoId;

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
    private Integer prodScale;

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
    private Integer storeStatus;

    /**
     * 拒绝理由
     */
    private String rejectReason;

    /**
     * 档口模板ID
     */
    @Excel(name = "档口模板ID")
    private Integer templateNum;

    /**
     * 档口权重值 基础值为0，最高100  最低-100
     */
    private Integer storeWeight;

    /**
     * 库存系统 1步橘 2天友 3发货宝
     */
    private Integer stockSys;
    /**
     * 档口浏览量
     */
    private Long viewCount;

}
