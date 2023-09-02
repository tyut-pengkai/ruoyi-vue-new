package com.ruoyi.web.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddTemplateRapidInnerVo {
    private Integer id;
    private String name;
    private BigDecimal price;
    private String remark;
}