package com.ruoyi.xkt.dto.userShoppingCart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("电商卖家进货车列表返回数据")
@Data

public class ShopCartPageResDTO {

    @ApiModelProperty(value = "主图")
    private String mainPicUrl;
    @ApiModelProperty(value = "进货单ID")
    private Long shoppingCartId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "商品标题")
    private String prodTitle;
    @ApiModelProperty(value = "商品状态")
    private String prodStatus;
    @ApiModelProperty(value = "进货车明细列表")
    List<ShopCartPageDetailResDTO> detailList;

}
