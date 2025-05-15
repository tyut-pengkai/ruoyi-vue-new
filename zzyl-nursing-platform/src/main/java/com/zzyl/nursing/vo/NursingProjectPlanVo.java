package com.zzyl.nursing.vo;

import com.zzyl.nursing.domain.NursingProjectPlan;
import lombok.Data;

import java.util.List;


@Data
public class NursingProjectPlanVo {
    private Long id;
    private Integer sortNo;
    private String planName;
    private Integer status;
    private List<NursingProjectPlan> projectPlans;
}
