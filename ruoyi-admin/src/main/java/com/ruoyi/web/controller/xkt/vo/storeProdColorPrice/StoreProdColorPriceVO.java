package com.ruoyi.web.controller.xkt.vo.storeProdColorPrice;

import com.ruoyi.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品当前颜色")
@Data
public class StoreProdColorPriceVO {

    @NotNull(message = "档口商品颜色ID不能为空!")
    @ApiModelProperty(name = "档口商品颜色ID")
    private Long storeColorId;
    @NotNull(message = "档口商品定价不能为空!")
    @ApiModelProperty(name = "档口商品定价")
    private BigDecimal price;

}
