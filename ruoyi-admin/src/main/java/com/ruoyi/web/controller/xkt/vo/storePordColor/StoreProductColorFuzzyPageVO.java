package com.ruoyi.web.controller.xkt.vo.storePordColor;

import com.ruoyi.web.controller.xkt.vo.BasePageVO;
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
@ApiModel
@Data
public class StoreProductColorFuzzyPageVO extends BasePageVO {

    @NotNull(message = "storeId不能为空")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;

}
