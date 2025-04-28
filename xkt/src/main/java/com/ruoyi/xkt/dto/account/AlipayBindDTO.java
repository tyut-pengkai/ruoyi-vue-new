package com.ruoyi.xkt.dto.account;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-23 14:41
 */
@Data
public class AlipayBindDTO {
    /**
     * 归属ID（平台=-1，档口=store_id，用户=user_id）
     */
    private Long ownerId;
    /**
     * 归属[1:平台 2:档口 3:用户]
     */
    private Integer ownerType;
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
