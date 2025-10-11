package com.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidExpertBean extends HybaseBean implements Serializable {


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
     * 项目编号
     */
    private String projectNo;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 行业
     */
    private String industry;

    /**
     * 品类
     */
    private String category;

    /**
     * 采购人信息名称
     */
    private String buyer;

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
     * 评审专家（源）
     */
    private String sourceExpert;

    /**
     * 评审专家
     */
    private String expert;


    private Integer sortMark;


    // 是否违规
    private Integer isIllegal;

}
