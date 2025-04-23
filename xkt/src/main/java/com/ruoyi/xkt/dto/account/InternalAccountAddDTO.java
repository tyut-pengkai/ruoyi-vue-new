package com.ruoyi.xkt.dto.account;

import lombok.Data;

/**
 * 内部账户
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.493
 **/
@Data
public class InternalAccountAddDTO {
    /**
     * 归属[1:平台 2:档口 3:用户]
     */
    private Integer ownerType;
    /**
     * 归属ID（平台=-1，档口=store_id）
     */
    private Long ownerId;
    /**
     * 备注
     */
    private String remark;
}
