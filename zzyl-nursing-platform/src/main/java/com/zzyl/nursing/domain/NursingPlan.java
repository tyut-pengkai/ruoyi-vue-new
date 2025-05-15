package com.zzyl.nursing.domain;

import com.zzyl.common.annotation.Excel;
import com.zzyl.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * 护理计划对象 nursing_plan
 *
 * @author ruoyi
 * @date 2025-05-14
 */
@Data
public class NursingPlan extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Integer id;

    /**
     * 排序号
     */
    private Integer sortNo;

    /**
     * 名称
     */
    @Excel(name = "名称")
    private String planName;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private Integer status;

    /**
     * 护理计划和项目关联信息
     */
    private List<NursingProjectPlan> nursingProjectPlanList;


}
