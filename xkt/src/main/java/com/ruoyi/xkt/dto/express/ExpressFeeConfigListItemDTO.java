package com.ruoyi.xkt.dto.express;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author liangyq
 * @date 2025-06-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExpressFeeConfigListItemDTO extends ExpressFeeConfigDTO {

    private String regionName;

    private String expressName;
}
