package com.ruoyi.system.domain.vo;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class UserBalanceChangeVo {

    private Long userId;
    private String operation;
    private BigDecimal amount;
    private String remark;

    @NotNull(message = "用户ID不能为空")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @NotNull(message = "调整类型不能为空")
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @NotNull(message = "调整金额不能为空")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @NotNull(message = "调整原因不能为空")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
