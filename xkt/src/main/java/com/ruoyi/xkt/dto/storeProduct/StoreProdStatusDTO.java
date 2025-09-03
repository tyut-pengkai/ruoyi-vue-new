package com.ruoyi.xkt.dto.storeProduct;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品状态")
@Data
public class StoreProdStatusDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口商品颜色ID")
    private List<Long> storeProdColorIdList;
    @ApiModelProperty(value = "档口商品状态")
    private Integer prodStatus;

}
