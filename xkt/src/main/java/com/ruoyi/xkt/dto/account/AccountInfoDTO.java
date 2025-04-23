package com.ruoyi.xkt.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-04-23 14:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountInfoDTO {
    /**
     * 内部账户
     */
    private InternalAccountDTO internalAccount;
    /**
     * 支付宝账户
     */
    private ExternalAccountDTO alipayAccount;
}
