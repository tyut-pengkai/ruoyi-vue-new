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
 * 公司组织信息对象 t_company
 * 
 * @author ruoyi
 * @date 2024-09-04
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_company")
public class TCompany extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 客户编号 */
    @Excel(name = "客户编号")
    private String khbh;

    /** 客户名称 */
    @Excel(name = "客户名称")
    private String khmc;

    /** 社会统一信用代码 */
    @Excel(name = "社会统一信用代码")
    private String shtydm;

    /** 管理员姓名 */
    @Excel(name = "管理员姓名")
    private String glyxm;

    /** 管理员电话 */
    @Excel(name = "管理员电话")
    private String glydh;

    /** 套餐情况 */
    @Excel(name = "套餐情况")
    private String tcqk;

    /** 打卡人数 */
    @Excel(name = "打卡人数")
    private Long dkrs;

    /** 权限开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "权限开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date qxkssj;

    /** 权限结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "权限结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date qxjssj;

    /** 状态 1 正常 0 停用 */
    @Excel(name = "状态 1 正常 0 停用")
    private String zt;

    /** 备注 */
    @Excel(name = "备注")
    private String bz;

    /** logo存储的url */
    @Excel(name = "logo存储的url")
    private String logo;

}
