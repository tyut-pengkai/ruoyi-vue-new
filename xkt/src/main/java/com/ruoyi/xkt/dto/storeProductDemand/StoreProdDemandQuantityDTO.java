package com.ruoyi.xkt.dto.storeProductDemand;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Builder
public class StoreProdDemandQuantityDTO {

    @ApiModelProperty(value = "货号")
    private Long storeId;
    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "档口商品颜色ID")
    private Long storeProdColorId;
    @ApiModelProperty(value = "货号")
    private String prodArtNum;
    @ApiModelProperty(value = "颜色")
    private String colorName;
    @ApiModelProperty(value = "今天是否提交过需求申请")
    private Boolean todaySubmitted;
    @ApiModelProperty(value = "数量对比列")
    private List<String> compareStrList;
    @ApiModelProperty(value = "尺码为30的数量")
    private List<Integer> size30List;
    @ApiModelProperty(value = "尺码为31的数量")
    private List<Integer> size31List;
    @ApiModelProperty(value = "尺码为32的数量")
    private List<Integer> size32List;
    @ApiModelProperty(value = "尺码为33的数量")
    private List<Integer> size33List;
    @ApiModelProperty(value = "尺码为34的数量")
    private List<Integer> size34List;
    @ApiModelProperty(value = "尺码为35的数量")
    private List<Integer> size35List;
    @ApiModelProperty(value = "尺码为36的数量")
    private List<Integer> size36List;
    @ApiModelProperty(value = "尺码为37的数量")
    private List<Integer> size37List;
    @ApiModelProperty(value = "尺码为38的数量")
    private List<Integer> size38List;
    @ApiModelProperty(value = "尺码为39的数量")
    private List<Integer> size39List;
    @ApiModelProperty(value = "尺码为40的数量")
    private List<Integer> size40List;
    @ApiModelProperty(value = "尺码为41的数量")
    private List<Integer> size41List;
    @ApiModelProperty(value = "尺码为42的数量")
    private List<Integer> size42List;
    @ApiModelProperty(value = "尺码为43的数量")
    private List<Integer> size43List;

}
