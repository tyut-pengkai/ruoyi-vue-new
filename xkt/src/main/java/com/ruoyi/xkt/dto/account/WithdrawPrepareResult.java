package com.ruoyi.xkt.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author liangyq
 * @date 2025-04-28 18:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawPrepareResult {
    /**
     * 单据ID
     */
    private Long financeBillId;
    /**
     * 单号
     */
    private String billNo;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 归属人实际账户
     */
    private String accountOwnerNumber;
    /**
     * 归属人真实姓名
     */
    private String accountOwnerName;
    /**
     * 归属人手机号
     */
    private String accountOwnerPhoneNumber;
}
