package com.common.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SupplierBean extends HybaseBean implements Serializable {

    private String uuid;

    private String rid;
    // 标准单位名称
    private String comName;
    // 标讯中的单位名称
    private String comBidName;
    // 供应商类型
    private String buyerType;
    // 法人
    private String legalPerson;
    // 成立日期
    private String esDate;
    //所属省份
    private String province;
    // 所属城市
    private String city;
    // 所属区县
    private String country;
    // 电话
    private String phone;
    // 统一社会信用代码
    private String creditCode;
    // 企业类型
    private String comType;
    //所属行业
    private String industry;
    // 地址
    private String address;
    // 经营范围
    private String scope;
    // 采购单位标记  前端用
    private String remark;
    // 入库时间
    private Date loadTime;

    private Boolean isCheck = false;

    private String recently;

    // 是否监控
    private Boolean isMonitor = false;

    // 是否违规
    private Integer isIllegal;

    private Integer sortMark;

}
