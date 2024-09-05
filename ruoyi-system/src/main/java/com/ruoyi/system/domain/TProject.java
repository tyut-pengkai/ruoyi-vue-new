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
 * 项目信息对象 t_project
 * 
 * @author ruoyi
 * @date 2024-09-04
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_project")
public class TProject extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String xmmc;

    /** 项目编号 */
    @Excel(name = "项目编号")
    private String xmbh;

    /** 负责人姓名 */
    @Excel(name = "负责人姓名")
    private String fzrxm;

    /** 负责人手机号 */
    @Excel(name = "负责人手机号")
    private String fzrsjhm;

    /** 项目开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "项目开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date xmkssj;

    /** 项目结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "项目结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date xmjssj;

    /** 项目状态 */
    @Excel(name = "项目状态")
    private String smzt;

    /** 项目描述 */
    @Excel(name = "项目描述")
    private String smms;

    /** 社会统一信用代码 */
    @Excel(name = "社会统一信用代码")
    private String shtydm;

}
