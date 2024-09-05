package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**
 * 项目申请信息对象 t_project_apply
 * 
 * @author ruoyi
 * @date 2024-09-04
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_project_apply")
public class TProjectApply extends BaseEntity {
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

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sqsj;

    /** 申请状态 0 待审批 1 通过 2 驳回 */
    @Excel(name = "申请状态 0 待审批 1 通过 2 驳回")
    private String sqzt;

}
