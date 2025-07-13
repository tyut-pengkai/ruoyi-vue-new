package com.ruoyi.web.controller.xkt.vo.storeProductDemand;

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
@Data
@ApiModel
public class StoreProdDemandPageVO extends BasePageVO {

    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空")
    private Long storeId;
    @ApiModelProperty(value = "需求状态:1 待生产 2 生产中 3 生产完成")
    private Integer demandStatus;
    @ApiModelProperty(value = "档口工厂ID")
    private Long storeFactoryId;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "是否紧急单", notes = "0=正常,1=紧急")
    private Integer emergency;

}
