package com.ruoyi.system.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.*;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**
 * 打卡信息对象 t_clockin_info
 * 
 * @author ruoyi
 * @date 2024-09-10
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_clockin_info")
public class TClockinInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 社会统一信用代码 */
    @Excel(name = "社会统一信用代码")
    private String shtydm;

    /** 项目编号 */
    @Excel(name = "项目编号")
    private String xmbh;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String xmmc;

    /** 姓名 */
    @Excel(name = "姓名")
    private String xm;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idcard;

    /** 打卡日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "打卡日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dkrq;

    /** 打卡开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "打卡开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dkkssj;

    /** 打卡结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "打卡结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dkjssj;

    /** 研发工时 */
    @Excel(name = "研发工时")
    private BigDecimal yfgs;

    /** 审核状态 0 待审核  1 通过 */
    @Excel(name = "审核状态 0 待审核  1 通过")
    private String shzt;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
