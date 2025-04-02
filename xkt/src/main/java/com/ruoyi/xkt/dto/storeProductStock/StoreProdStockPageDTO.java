package com.ruoyi.xkt.dto.storeProductStock;

import com.ruoyi.xkt.dto.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("档口库存分页查询入参")
@Data
public class StoreProdStockPageDTO extends BasePageDTO {

    @ApiModelProperty(name = "档口ID")
    private Long storeId;
    @ApiModelProperty(name = "商品货号")
    private String prodArtNum;

}
