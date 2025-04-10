package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.Version;
import com.ruoyi.common.core.domain.SimpleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 外部账户交易明细
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.470
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExternalAccountTransDetail extends SimpleEntity {
    /**
     * 外部账户ID
     */
    private Long externalAccountId;
    /**
     * 来源单据ID
     */
    private Long srcBillId;
    /**
     * 来源单据类型[2:付款]
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
     * 备注
     */
    private String remark;
    /**
     * 版本号
     */
    @Version
    private Long version;
}
