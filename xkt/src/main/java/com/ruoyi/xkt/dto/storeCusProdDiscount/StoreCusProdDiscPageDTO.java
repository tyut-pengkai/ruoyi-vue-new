package com.ruoyi.xkt.dto.storeCusProdDiscount;

import com.ruoyi.xkt.dto.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreCusProdDiscPageDTO extends BasePageDTO {

    @ApiModelProperty(value = "客户名称")
    private String cusName;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;

}
