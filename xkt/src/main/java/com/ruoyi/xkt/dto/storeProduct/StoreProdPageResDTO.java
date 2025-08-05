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

    @ApiModelProperty(value = "档口商品颜色ID")
    private Long storeProdColorId;
    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "颜色ID")
    private Long storeColorId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口商品主图url")
    private String mainPicUrl;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "颜色")
    private String colorName;
    @ApiModelProperty(name = "商品分类ID")
    private Long prodCateId;
    @ApiModelProperty(value = "分类类目")
    private String prodCateName;
    @ApiModelProperty(value = "标准尺码")
    private String standard;
    @ApiModelProperty(value = "销售金额（元）")
    private BigDecimal price;
    @ApiModelProperty(value = "状态:1未发布，2在售，3尾货，4已下架，5已删除")
    private Integer prodStatus;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}
