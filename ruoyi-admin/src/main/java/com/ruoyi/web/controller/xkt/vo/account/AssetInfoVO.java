package com.ruoyi.web.controller.xkt.vo.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-23 14:17
 */
@ApiModel
@Data
public class AssetInfoVO {
    /**
     * 内部账户
     */
    @ApiModelProperty(value = "内部账户")
    private InternalAccountVO internalAccount;
    /**
     * 支付宝账户
     */
    @ApiModelProperty(value = "支付宝账户")
    private ExternalAccountVO alipayAccount;
}
