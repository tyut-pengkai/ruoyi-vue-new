package com.ruoyi.xkt.dto.finance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liangyq
 * @date 2025-04-09 23:47
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransInfo {
//    /**
//     * 账户ID
//     */
//    private Long accountId;
    /**
     * 来源单据ID
     */
    private Long srcBillId;
    /**
     * 来源单据类型
     *
     * @see com.ruoyi.xkt.enums.EFinBillType
     */
    private Integer srcBillType;
//    /**
//     * 借贷方向
//     *
//     * @see com.ruoyi.xkt.enums.ELoanDirection
//     */
//    private Integer loanDirection;
    /**
     * 交易金额
     */
    private BigDecimal transAmount;
    /**
     * 交易时间
     */
    private Date transTime;
    /**
     * 经办人ID
     */
    private Long handlerId;
//    /**
//     * 入账状态
//     *
//     * @see com.ruoyi.xkt.enums.EEntryStatus
//     */
//    private Integer entryStatus;
    /**
     * 备注
     */
    private String remark;
}
