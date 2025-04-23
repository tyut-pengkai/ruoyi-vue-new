package com.ruoyi.xkt.dto.account;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-23 15:03
 */
@Data
public class ExternalAccountAddDTO {
    /**
     * 归属[1:平台 2:档口 3:用户]
     */
    private Integer ownerType;
    /**
     * 归属ID（平台=-1，档口=store_id，用户=user_id）
     */
    private Long ownerId;
    /**
     * 账户类型[1:支付宝账户]
     */
    private Integer accountType;
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
    /**
     * 归属人认证通过
     */
    private Boolean accountAuthAccess;
    /**
     * 备注
     */
    private String remark;
}
