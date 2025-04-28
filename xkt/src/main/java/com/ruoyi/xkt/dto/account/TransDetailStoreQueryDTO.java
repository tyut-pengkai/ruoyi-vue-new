package com.ruoyi.xkt.dto.account;

import com.ruoyi.xkt.dto.BasePageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author liangyq
 * @date 2025-04-28 18:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TransDetailStoreQueryDTO extends BasePageDTO {
    /**
     * 档口ID
     */
    private Long storeId;
}
