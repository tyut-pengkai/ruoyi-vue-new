package com.ruoyi.web.controller.common.vo;

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
public class IdCardVO {
    /**
     * 身份证号码
     */
    @ApiModelProperty("身份证号码")
    private String idNumber;
    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;
    /**
     * 性别
     */
    @ApiModelProperty("性别")
    private String sex;
    /**
     * 民族
     */
    @ApiModelProperty("民族")
    private String ethnicity;
    /**
     * 出生日期
     */
    @ApiModelProperty("出生日期")
    private String birthDate;
    /**
     * 住址
     */
    @ApiModelProperty("住址")
    private String address;

    /**
     * 签发机关
     */
    @ApiModelProperty("签发机关")
    private String issueAuthority;
    /**
     * 有效期限
     */
    @ApiModelProperty("有效期限")
    private String validPeriod;
}
