package com.ruoyi.framework.ocr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-06-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdCard {
    /**
     * 身份证号码
     */
    private String idNumber;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String sex;
    /**
     * 民族
     */
    private String ethnicity;
    /**
     * 出生日期
     */
    private String birthDate;
    /**
     * 住址
     */
    private String address;

    /**
     * 签发机关
     */
    private String issueAuthority;
    /**
     * 有效期限
     */
    private String validPeriod;
}
