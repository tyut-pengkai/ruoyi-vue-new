package com.ruoyi.web.controller.xkt.vo.storeCusProdDiscount;

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
@ApiModel("档口客户销售分页查询入参")
@Data
public class StoreCusProdDiscPageVO extends BasePageVO {

    @ApiModelProperty(value = "客户名称")
    private String cusName;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空")
    private Long storeId;

}
