package com.common.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 公告实体
 *
 * @author TRS
 */
@Data
public class BidDocumentBean extends HybaseBean implements Serializable {


    private String uuid;

    /**
     * 唯一值ID
     */
    private String rid;

    /**
     * 公告发布时间
     */
    private Date pubTime;

    /**
     * 标题
     */
    private String title;

    /**
     * 项目编号
     */
    private String projectNo;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目概括
     */
    private String projectAbs;

    /**
     * 预算金额
     */
    private String budgetAmount;

    /**
     * 行业
     */
    private String industry;

    /**
     * 品类
     */
    private String category;

    /**
     * 是否接受联合体投标
     */
    private String isAccept;

    /**
     * 获取招标文件时间
     */
    private Date bidDate;

    /**
     * 提交投标文件截止时间
     */
    private Date bidDeadlineDate;

    /**
     * 采购人信息名称
     */
    private String buyer;

    /**
     * 采购人地址
     */
    private String buyerAddr;

    /**
     * 采购人负责人
     */
    private String buyerPerson;

    /**
     * 采购人联系方式
     */
    private String buyerPhone;

    /**
     * 采购人所属省
     */
    private String buyerProvince;

    /**
     * 采购人所属市
     */
    private String buyerCity;

    /**
     * 采购人所属区县
     */
    private String buyerCounty;

    /**
     * 代理机构
     */
    private String agent;

    /**
     * 代理机构地址
     */
    private String agentAddr;

    /**
     * 代理机构负责人
     */
    private String agentPerson;

    /**
     * 代理机构联系方式
     */
    private String agentPhone;

    /**
     * 代理机构所属省
     */
    private String agentProvince;

    /**
     * 代理机构所属市
     */
    private String agentCity;

    /**
     * 代理机构所属区县
     */
    private String agentCounty;

    /**
     * 项目联系人
     */
    private String projectPeople;

    /**
     * 项目联系人电话
     */
    private String projectPhone;

    /**
     * 采购方式
     */
    private String purchaseMethod;

    /**
     * 品目名称
     */
    private String itemName;

    /**
     * 采购标的
     */
    private String purchaseObject;

    /**
     * 采购计划备案号
     */
    private String purchaseRecord;

    /**
     * 中标供应商
     */
    private String supplier;

    /**
     * 中标供应商地址
     */
    private String supplierAddr;

    /**
     * 中标供应商省
     */
    private String supplierProvince;

    /**
     * 中标供应商市
     */
    private String supplierCity;

    /**
     * 中标供应商区县
     */
    private String supplierCounty;

    /**
     * 货物名称
     */
    private String goodsName;

    /**
     * 评审专家
     */
    private String expert;

    /**
     * 服务名称
     */
    private String serveName;

    /**
     * 服务范围
     */
    private String serveRange;

    /**
     * 更正内容
     */
    private String correctContent;

    /**
     * 更正后内容
     */
    private String correctLaterContent;

    /**
     * 原文链接
     */
    private String urlName;

    /**
     * 站点
     */
    private String siteName;

    /**
     * 正文
     */
    private String content;

    /**
     * 中标金额
     */
    private String winBidAmount;

    /**
     * 公告类型
     */
    private String bidType;

    /**
     * 公告类型小类
     */
    private String bidTypeSmall;

    /**
     * 国标标签
     */
    private String gbTag;
    /**
     * 新质生产力标签
     */
    private String xzTag;


    private Long isMonitor;

    private Long isCollect;

    /**
     * 行政级别
     */
    private String buyerGrade;
    /**
     * 采购单位类型
     */
    private String buyerType;

    /**
     * 是否新监控
     */
    private Boolean isNew;

    private Integer sortMark;

}
