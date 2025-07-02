package com.ruoyi.web.controller.xkt.vo.storeProductDemand;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Accessors(chain = true)
public class StoreProdDemandPageResVO {

    @ApiModelProperty(value = "storeProdDemandId")
    private Long storeProdDemandId;
    @ApiModelProperty(value = "storeProdDemandDetailId")
    private Long storeProdDemandDetailId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口工厂名称")
    private String storeFactoryName;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "编号")
    private String code;
    @ApiModelProperty(value = "货号")
    private String prodArtNum;
    @ApiModelProperty(value = "颜色")
    private String colorName;
    @ApiModelProperty(value = "生产状态")
    private String detailStatus;
    @ApiModelProperty(value = "紧急单还是正常单")
    private Integer emergency;
    @ApiModelProperty(value = "计划生产数量")
    private Integer quantity;
    @ApiModelProperty(value = "入库数量")
    private Integer storageQuantity;
    @ApiModelProperty(value = "生产中数量")
    private Integer inProdQuantity;
    @ApiModelProperty(value = "尺码30")
    private Integer size30;
    @ApiModelProperty(value = "尺码31")
    private Integer size31;
    @ApiModelProperty(value = "尺码32")
    private Integer size32;
    @ApiModelProperty(value = "尺码33")
    private Integer size33;
    @ApiModelProperty(value = "尺码34")
    private Integer size34;
    @ApiModelProperty(value = "尺码35")
    private Integer size35;
    @ApiModelProperty(value = "尺码36")
    private Integer size36;
    @ApiModelProperty(value = "尺码37")
    private Integer size37;
    @ApiModelProperty(value = "尺码38")
    private Integer size38;
    @ApiModelProperty(value = "尺码39")
    private Integer size39;
    @ApiModelProperty(value = "尺码40")
    private Integer size40;
    @ApiModelProperty(value = "尺码41")
    private Integer size41;
    @ApiModelProperty(value = "尺码42")
    private Integer size42;
    @ApiModelProperty(value = "尺码43")
    private Integer size43;

}
