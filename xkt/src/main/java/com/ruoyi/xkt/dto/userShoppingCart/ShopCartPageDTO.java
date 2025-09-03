package com.ruoyi.xkt.dto.userShoppingCart;

import com.ruoyi.xkt.dto.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("电商卖家新增进货车")
@Data

public class ShopCartPageDTO extends BasePageDTO {

    @ApiModelProperty(value = "userId")
    private Long userId;
    @ApiModelProperty(value = "商品状态，在售传：2， 已失效传：4,5")
    List<Integer> statusList;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "档口名称")
    private String storeName;

}
