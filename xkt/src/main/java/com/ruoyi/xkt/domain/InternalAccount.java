package com.ruoyi.xkt.domain;

import com.ruoyi.common.core.domain.SimpleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 内部账户
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.493
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InternalAccount extends SimpleEntity {
    /**
     * 归属[1:平台 2:档口 3:用户]
     */
    private Integer ownerType;
    /**
     * 归属ID（平台=-1，档口=store_id）
     */
    private Long ownerId;
    /**
     * 账户状态[1:正常 2:冻结]
     */
    private Integer accountStatus;
    /**
     * 交易密码
     */
    private String transactionPassword;
    /**
     * 电话号码
     */
    private String phoneNumber;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 可用余额
     */
    private BigDecimal usableBalance;
    /**
     * 支付中金额
     */
    private BigDecimal paymentAmount;
    /**
     * 备注
     */
    private String remark;
}
