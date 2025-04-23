package com.ruoyi.xkt.dto.account;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-23 14:41
 */
@Data
public class AlipayBindDTO {
    /**
     * 档口ID
     */
    private Long storeId;
    /**
     * 账号
     */
    private String accountOwnerNumber;
    /**
     * 姓名
     */
    private String accountOwnerName;
    /**
     * 手机号
     */
    private String accountOwnerPhoneNumber;
    /**
     * 验证码
     */
    private String verifyCode;
}
