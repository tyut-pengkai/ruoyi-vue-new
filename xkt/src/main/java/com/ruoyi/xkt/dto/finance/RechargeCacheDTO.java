package com.ruoyi.xkt.dto.finance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author liangyq
 * @date 2025-08-25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RechargeCacheDTO implements Serializable {
    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 金额
     */
    private BigDecimal amount;
}
