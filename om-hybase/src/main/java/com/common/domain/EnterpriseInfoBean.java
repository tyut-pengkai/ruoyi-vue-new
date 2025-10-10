package com.common.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 工商主体基本信息表
 *
 * @author TRS
 */
@Data
public class EnterpriseInfoBean extends HybaseBean implements Serializable {

    /**
     * 自增Id
     */
    private Long id;

    /**
     * 主键
     */
    private String recId;

    /**
     * 企业EID
     */
    private String eid;

    /**
     * 实体名称
     */
    private String entName;

    /**
     * 统一社会信用代码
     */
    private String usccCode;

    /**
     * 工商注册号
     */
    private String regNo;

    /**
     * 组织机构代码
     */
    private String nacaoCode;

    /**
     * 企业(机构)类型
     */
    private String entTypeCode;

    /**
     * 许可经营项目
     */
    private String abuItem;

    /**
     * 一般经营项目
     */
    private String cbuItem;

    /**
     * 经营(驻在)期限自
     */
    private Date opFromDate;

    /**
     * 经营(驻在)期限至
     */
    private Date opToDate;

    /**
     * 邮政编码
     */
    private String postalCode;

    /**
     * 成立日期
     */
    private Date esDate;

    /**
     * 核准日期
     */
    private Date apprDate;

    /**
     * 登记机关
     */
    private String regOrg;

    /**
     * 企业状态
     */
    private String entStatusCode;

    /**
     * 注册资金(万)
     */
    private Double regCap;

    /**
     * 注册资本(金)币种
     */
    private String regCapCurCode;

    /**
     * 经营(业务)范围
     */
    private String opScope;

    /**
     * 经营方式
     */
    private String opForm;

    /**
     * 经营范围及方式
     */
    private String opScoandForm;

    /**
     * 兼营范围
     */
    private String ptbusScope;

    /**
     * 住所
     */
    private String domAddr;

    /**
     * 实收资本(万)
     */
    private Double recCap;

    /**
     * 法人姓名
     */
    private String corporationName;

    /**
     * 法人称谓
     */
    private String corporationTitle;

    /**
     * 注销日期
     */
    private Date canDate;

    /**
     * 吊销日期
     */
    private Date revDate;

    /**
     * 最后年检日期
     */
    private String ancheYearDate;

    /**
     * 行政地区代码
     */
    private String regionId;

    /**
     * 行业分类代码
     */
    private String nicId;

    /**
     * 经度(高德)
     */
    private String lat;

    /**
     * 纬度(高德)
     */
    private String lng;

    /**
     * 数据更新时间
     */
    private Date updateTime;

    /**
     * 数据软删除标识(1删除，0保留)
     */
    private String deleteFlg;

    /**
     * 数据删除时间
     */
    private Date deleteTime;


    private Long isMonitor;

}
