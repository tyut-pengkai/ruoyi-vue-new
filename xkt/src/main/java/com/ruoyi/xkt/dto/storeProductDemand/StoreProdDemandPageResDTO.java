package com.ruoyi.xkt.dto.storeProductDemand;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品需求分页返回数据")
@Data
@Accessors(chain = true)
public class StoreProdDemandPageResDTO {

    @ApiModelProperty(name = "storeProdDemandId")
    private Long storeProdDemandId;
    @ApiModelProperty(name = "storeProdDemandDetailId")
    private Long storeProdDemandDetailId;
    @ApiModelProperty(name = "档口ID")
    private Long storeId;
    @ApiModelProperty(name = "档口工厂名称")
    private String storeFactoryName;
    @ApiModelProperty(name = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(name = "编号")
    private String code;
    @ApiModelProperty(name = "货号")
    private String prodArtNum;
    @ApiModelProperty(name = "颜色")
    private String colorName;
    @ApiModelProperty(name = "生产状态")
    private String detailStatus;
    @ApiModelProperty(name = "紧急单还是正常单")
    private String emergency;
    @ApiModelProperty(name = "计划生产数量")
    private Integer quantity;
    @ApiModelProperty(name = "入库数量")
    private Integer storageQuantity;
    @ApiModelProperty(name = "生产中数量")
    private Integer inProdQuantity;
    @ApiModelProperty(name = "尺码30")
    private Integer size30;
    @ApiModelProperty(name = "尺码31")
    private Integer size31;
    @ApiModelProperty(name = "尺码32")
    private Integer size32;
    @ApiModelProperty(name = "尺码33")
    private Integer size33;
    @ApiModelProperty(name = "尺码34")
    private Integer size34;
    @ApiModelProperty(name = "尺码35")
    private Integer size35;
    @ApiModelProperty(name = "尺码36")
    private Integer size36;
    @ApiModelProperty(name = "尺码37")
    private Integer size37;
    @ApiModelProperty(name = "尺码38")
    private Integer size38;
    @ApiModelProperty(name = "尺码39")
    private Integer size39;
    @ApiModelProperty(name = "尺码40")
    private Integer size40;
    @ApiModelProperty(name = "尺码41")
    private Integer size41;
    @ApiModelProperty(name = "尺码42")
    private Integer size42;
    @ApiModelProperty(name = "尺码43")
    private Integer size43;

}
