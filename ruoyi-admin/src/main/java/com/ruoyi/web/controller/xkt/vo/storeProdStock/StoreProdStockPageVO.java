package com.ruoyi.web.controller.xkt.vo.storeProdStock;

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
@ApiModel("档口库存分页查询入参")
@Data
public class StoreProdStockPageVO extends BasePageVO {

    @ApiModelProperty(name = "档口ID")
    @NotNull(message = "档口ID不能为空")
    private Long storeId;
    @ApiModelProperty(name = "商品货号")
    private String prodArtNum;

}
