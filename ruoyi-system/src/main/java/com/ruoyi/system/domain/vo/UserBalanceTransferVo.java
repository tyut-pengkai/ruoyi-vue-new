package com.ruoyi.system.domain.vo;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class UserBalanceTransferVo {

    private Long toUserId;
    private BigDecimal amount;
    private String remark;

    @NotNull(message = "目的账号不能为空")
    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    @NotNull(message = "转账金额不能为空")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @NotNull(message = "转账原因不能为空")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
