package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

/**
 * 日志信息对象 t_clockin_work_log
 * 
 * @author ruoyi
 * @date 2024-09-04
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_clockin_work_log")
public class TClockinWorkLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 打卡id */
    @Excel(name = "打卡id")
    private Long dkid;

    /** 日志内容 */
    @Excel(name = "日志内容")
    private String content;

    /** 附件名称 */
    @Excel(name = "附件名称")
    private String fjmc;

    /** 附件地址 */
    @Excel(name = "附件地址")
    private String fjdz;

    /** 附件类型 */
    @Excel(name = "附件类型")
    private String fjlx;

}
