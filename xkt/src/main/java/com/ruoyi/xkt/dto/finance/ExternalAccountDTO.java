package com.ruoyi.xkt.dto.finance;

import lombok.Data;

import java.util.Date;

/**
 * 外部账户
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.450
 **/
@Data
public class ExternalAccountDTO {
    /**
     * 外部账户ID
     */
    private Long id;
    /**
     * 归属[1:平台 2:档口 3:用户]
     */
    private Integer ownerType;
    /**
     * 归属ID（平台=-1，档口=store_id，用户=user_id）
     */
    private Long ownerId;
    /**
     * 账户状态[1:正常 2:冻结]
     */
    private Integer accountStatus;
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
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
}
