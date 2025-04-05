package com.ruoyi.xkt.dto.storeProduct;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品列表数据")
@Data
@Accessors(chain = true)
public class StoreProdPageResDTO {

    @ApiModelProperty(name = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(name = "档口ID")
    private Long storeId;
    @ApiModelProperty(name = "档口商品主图url")
    private String mainPicUrl;
    @ApiModelProperty(name = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(name = "颜色")
    private String colorName;

    private Long prodCateId;

    @ApiModelProperty(name = "分类类目")
    private String prodCateName;
    @ApiModelProperty(name = "标准尺码")
    private List<Integer> standardSizeList;
    @ApiModelProperty(name = "销售金额（元）")
    private BigDecimal price;
    @ApiModelProperty(name = "状态")
    private Integer prodStatus;
    @ApiModelProperty(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
