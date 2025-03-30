package com.ruoyi.web.controller.xkt.vo.storeProd;

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
@ApiModel("档口商品分页查询入参")
@Data
public class StoreProdPageVO extends BasePageVO {

    @ApiModelProperty(name = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(name = "商品分类ID")
    private Long prodCateId;
    @ApiModelProperty(name = "档口ID")
    @NotNull(message = "档口ID不能为空")
    private Long storeId;

}
