package com.ruoyi.xkt.dto.storeProduct;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品各个状态数量")
@Data

public class StoreProdStatusCountResDTO {

    @ApiModelProperty(value = "未发布数量")
    private Integer unPublishedNum;
    @ApiModelProperty(value = "在售数量")
    private Integer onSaleNum;
    @ApiModelProperty(value = "尾货数量")
    private Integer tailGoodsNum;
    @ApiModelProperty(value = "已下架数量")
    private Integer offSaleNum;
    @ApiModelProperty(value = "已删除数量")
    private Integer removedNum;

}
