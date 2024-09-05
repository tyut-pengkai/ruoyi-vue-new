package com.ruoyi.system.domain;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**
 * 打卡时间设置对象 t_clockin_set
 * 
 * @author ruoyi
 * @date 2024-09-04
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_clockin_set")
public class TClockinSet extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 打卡编号 */
    @Excel(name = "打卡编号")
    private String dkbh;

    /** 上午开始时间 */
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    @Excel(name = "上午开始时间", width = 30, dateFormat = "HH:mm")
    private Date swkssj;

    /** 上午结束时间 */
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    @Excel(name = "上午结束时间", width = 30, dateFormat = "HH:mm")
    private Date swjssj;

    /** 下午开始时间 */
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    @Excel(name = "下午开始时间", width = 30, dateFormat = "HH:mm")
    private Date xwkssj;

    /** 下午结束时间 */
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    @Excel(name = "下午结束时间", width = 30, dateFormat = "HH:mm")
    private Date xwjssj;

    /** 上班总工时 */
    @Excel(name = "上班总工时")
    private String sbzgs;

    /** 执行开始时间 */
    @JsonFormat(pattern = "MM-dd HH:mm",timezone = "GMT+8")
    @Excel(name = "执行开始时间", width = 30, dateFormat = "MM-dd HH:mm")
    private Date zxkssj;

    /** 执行结束时间 */
    @JsonFormat(pattern = "MM-dd HH:mm",timezone = "GMT+8")
    @Excel(name = "执行结束时间", width = 30, dateFormat = "MM-dd HH:mm")
    private Date zxjssj;

    /** 备注 */
    @Excel(name = "备注")
    private String bz;

    /** 社会统一信用代码 */
    @Excel(name = "社会统一信用代码")
    private String shtydm;

}
