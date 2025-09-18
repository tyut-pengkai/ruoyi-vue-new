package com.ruoyi.xkt.dto.storeProductStock;

import com.ruoyi.xkt.dto.BasePageDTO;
import io.swagger.annotations.ApiModel;
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
@ApiModel
public class StoreProdWebsiteStockPageDTO extends BasePageDTO {

    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;

}
