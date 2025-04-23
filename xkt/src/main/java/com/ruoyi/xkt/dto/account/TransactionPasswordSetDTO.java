package com.ruoyi.xkt.dto.account;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-23 15:32
 */
@Data
public class TransactionPasswordSetDTO {
    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 手机号
     */
    private String phoneNumber;
    /**
     * 验证码
     */
    private String verifyCode;
    /**
     * 交易密码
     */
    private String transactionPassword;
}
