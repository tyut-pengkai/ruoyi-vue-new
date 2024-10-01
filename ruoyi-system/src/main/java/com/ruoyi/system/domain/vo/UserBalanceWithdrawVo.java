package com.ruoyi.system.domain.vo;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class UserBalanceWithdrawVo {

    private BigDecimal applyFee;
    private Long receiveAccountId;
    private String remark;

    @NotNull(message = "提现金额不能为空")
    public BigDecimal getApplyFee() {
        return applyFee;
    }

    public void setApplyFee(BigDecimal applyFee) {
        this.applyFee = applyFee;
    }

    @NotNull(message = "收款账号不能为空")
    public Long getReceiveAccountId() {
        return receiveAccountId;
    }

    public void setReceiveAccountId(Long receiveAccountId) {
        this.receiveAccountId = receiveAccountId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
