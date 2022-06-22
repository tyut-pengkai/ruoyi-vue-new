package com.ruoyi.system.domain.vo;

import com.ruoyi.common.enums.BalanceChangeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceChangeVo {

    private Long userId;
    private Long sourceUserId;
    private BalanceChangeType type;
    private String description;
    private String updateBy;
    private Long saleOrderId;
    private Long withdrawCashId;
    private BigDecimal availablePayBalance = BigDecimal.ZERO;
    private BigDecimal freezePayBalance = BigDecimal.ZERO;
    private BigDecimal availableFreeBalance = BigDecimal.ZERO;
    private BigDecimal freezeFreeBalance = BigDecimal.ZERO;

}
