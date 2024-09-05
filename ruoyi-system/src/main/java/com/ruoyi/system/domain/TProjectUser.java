package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**
 * 项目人员关联对象 t_project_user
 * 
 * @author ruoyi
 * @date 2024-09-04
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_project_user")
public class TProjectUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 社会统一信用代码 */
    @Excel(name = "社会统一信用代码")
    private String shtydm;

    /** 项目编号 */
    @Excel(name = "项目编号")
    private String smbh;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String smmc;

    /** 姓名 */
    @Excel(name = "姓名")
    private String xm;

    /** 身份证号 */
    @Excel(name = "身份证号")
    private String idcard;

    /** 项目角色 */
    @Excel(name = "项目角色")
    private String xmjs;

    /** 角色id */
    @Excel(name = "角色id")
    private Long xmjsid;

    /** 劳务关系 */
    @Excel(name = "劳务关系")
    private String lwgx;

    /** 所属部门 */
    @Excel(name = "所属部门")
    private String ssbm;

    /** 岗位 */
    @Excel(name = "岗位")
    private String gw;

    /** 学历 */
    @Excel(name = "学历")
    private String xl;

    /** 职称 */
    @Excel(name = "职称")
    private String zc;

    /** 备注 */
    @Excel(name = "备注")
    private String bz;

    /** 启用状态  0 禁用  1 启用 */
    @Excel(name = "启用状态  0 禁用  1 启用")
    private String qyzt;

}
