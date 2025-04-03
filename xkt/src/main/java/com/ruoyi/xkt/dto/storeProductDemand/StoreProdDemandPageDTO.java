package com.ruoyi.xkt.dto.storeProductDemand;

import com.ruoyi.xkt.dto.BasePageDTO;
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
@ApiModel("档口需求分页查询入参")
@Data
public class StoreProdDemandPageDTO extends BasePageDTO {

    @ApiModelProperty(name = "档口ID")
    @NotNull(message = "档口ID不能为空")
    private Long storeId;
    @ApiModelProperty(name = "档口工厂ID")
    private Long storeFactoryId;
    @ApiModelProperty(name = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(name = "备注")
    private String remark;
    @ApiModelProperty(name = "是否紧急单", notes = "0=正常,1=紧急")
    private Integer emergency;

}
