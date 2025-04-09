package com.ruoyi.xkt.dto.storeProduct;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口首页返回数据")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreProdStatusCountDTO {

    @ApiModelProperty(value = "在售数量")
    private Integer onSaleNum;
    @ApiModelProperty(value = "尾货数量")
    private Integer tailGoodsNum;
    @ApiModelProperty(value = "已下架数量")
    private Integer offSaleNum;

}
