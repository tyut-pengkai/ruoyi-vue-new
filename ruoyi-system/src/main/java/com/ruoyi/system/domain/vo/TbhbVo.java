package com.ruoyi.system.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TbhbVo {

    private String dateTime = "-";
    private BigDecimal sumNum = BigDecimal.ZERO;
    private BigDecimal tbSumNum = BigDecimal.ZERO;
    private BigDecimal hbSumNum = BigDecimal.ZERO;
    private String tbRate = "-";
    private String hbRate = "-";

}
