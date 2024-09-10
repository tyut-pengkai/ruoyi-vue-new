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
 * 日志提醒对象 t_notice
 * 
 * @author ruoyi
 * @date 2024-09-09
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_notice")
public class TNotice implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 提醒日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "提醒日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date txrq;

    /** 姓名 */
    @Excel(name = "姓名")
    private String xm;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idcard;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 提醒内容 */
    @Excel(name = "提醒内容")
    private String content;

    /** 阅读状态 0 未读 1 已读 */
    @Excel(name = "阅读状态 0 未读 1 已读")
    private String ydzt;

    /** 社会统一代码 */
    @Excel(name = "社会统一代码")
    private String shtydm;

    /** 项目编号 */
    @Excel(name = "项目编号")
    private String xmbh;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String smmc;

    /** 项目状态 */
    @Excel(name = "项目状态")
    private String xmzt;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
