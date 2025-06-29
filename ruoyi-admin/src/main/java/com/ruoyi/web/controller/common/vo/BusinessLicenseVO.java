package com.ruoyi.web.controller.common.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-06-29
 */
@ApiModel
@Data
public class BusinessLicenseVO {
    /**
     * 统一社会信用代码
     */
    @ApiModelProperty("统一社会信用代码")
    private String creditCode;
    /**
     * 营业名称
     */
    @ApiModelProperty("营业名称")
    private String companyName;
    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String companyType;
    /**
     * 营业场所/住所
     */
    @ApiModelProperty("营业场所/住所")
    private String businessAddress;
    /**
     * 法人/负责人
     */
    @ApiModelProperty("法人/负责人")
    private String legalPerson;
    /**
     * 经营范围
     */
    @ApiModelProperty("经营范围")
    private String businessScope;
    /**
     * 注册资本
     */
    @ApiModelProperty("注册资本")
    private String registeredCapital;
    /**
     * 注册日期
     */
    @ApiModelProperty("注册日期")
    private String registrationDate;
    /**
     * 营业期限
     */
    @ApiModelProperty("营业期限")
    private String validPeriod;
    /**
     * 格式化营业期限起始日期
     */
    @ApiModelProperty("格式化营业期限起始日期")
    private String validFromDate;
    /**
     * 格式化营业期限终止日期
     */
    @ApiModelProperty("格式化营业期限终止日期")
    private String validToDate;
    /**
     * 组成形式
     */
    @ApiModelProperty("组成形式")
    private String companyForm;
    /**
     * 发照日期
     */
    @ApiModelProperty("发照日期")
    private String issueDate;
    /**
     * 证照标题
     */
    @ApiModelProperty("证照标题")
    private String title;
}
