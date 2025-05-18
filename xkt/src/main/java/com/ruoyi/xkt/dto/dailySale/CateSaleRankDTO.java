package com.ruoyi.xkt.dto.dailySale;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("首页销售榜数据")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
public class CateSaleRankDTO {

    @ApiModelProperty(value = "storeProdId")
    private Long storeProdId;
    @ApiModelProperty(value = "货号")
    private String prodArtNum;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "商品分类ID")
    private Long prodCateId;
    @ApiModelProperty(value = "商品分类名称")
    private String prodCateName;
    @ApiModelProperty(value = "销售数量")
    private Integer saleNum;

}
