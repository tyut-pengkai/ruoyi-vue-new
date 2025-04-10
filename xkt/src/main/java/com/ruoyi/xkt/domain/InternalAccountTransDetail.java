package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.Version;
import com.ruoyi.common.core.domain.SimpleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 内部账户交易明细
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.516
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InternalAccountTransDetail extends SimpleEntity {
    /**
     * 内部账户ID
     */
    private Long internalAccountId;
    /**
     * 来源单据ID
     */
    private Long srcBillId;
    /**
     * 来源单据类型[1:收款 2:付款 3:转移]
     */
    private Integer srcBillType;
    /**
     * 借贷方向[1:借(D) 2:贷(C)]
     */
    private Integer loanDirection;
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
    /**
     * 入账状态 [1:已入账 2:未入账]
     */
    private Integer entryStatus;
    /**
     * 入账执行标识[1:已执行 2:未执行]
     */
    private Integer entryExecuted;
    /**
     * 备注
     */
    private String remark;
    /**
     * 版本号
     */
    @Version
    private Long version;
}
