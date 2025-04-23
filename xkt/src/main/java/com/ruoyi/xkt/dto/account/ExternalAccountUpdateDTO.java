package com.ruoyi.xkt.dto.account;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-23 15:03
 */
@Data
public class ExternalAccountUpdateDTO {
    /**
     * ID
     */
    private Long id;
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
