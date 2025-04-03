package com.ruoyi.web.controller.xkt.vo.storeProductDemand;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品在产数量及库存数量")
@Data
@Builder
public class StoreProdDemandQuantityVO {

    @ApiModelProperty(name = "货号")
    private Long storeId;
    @ApiModelProperty(name = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(name = "档口商品颜色ID")
    private Long storeProdColorId;
    @ApiModelProperty(name = "货号")
    private String prodArtNum;
    @ApiModelProperty(name = "颜色")
    private String colorName;
    @ApiModelProperty(name = "今天是否提交过需求申请")
    private Boolean todaySubmitted;
    @ApiModelProperty(name = "数量对比列")
    private List<String> compareStrList;
    @ApiModelProperty(name = "尺码为30的数量")
    private List<String> size30List;
    @ApiModelProperty(name = "尺码为31的数量")
    private List<String> size31List;
    @ApiModelProperty(name = "尺码为32的数量")
    private List<String> size32List;
    @ApiModelProperty(name = "尺码为33的数量")
    private List<String> size33List;
    @ApiModelProperty(name = "尺码为34的数量")
    private List<String> size34List;
    @ApiModelProperty(name = "尺码为35的数量")
    private List<String> size35List;
    @ApiModelProperty(name = "尺码为36的数量")
    private List<String> size36List;
    @ApiModelProperty(name = "尺码为37的数量")
    private List<String> size37List;
    @ApiModelProperty(name = "尺码为38的数量")
    private List<String> size38List;
    @ApiModelProperty(name = "尺码为39的数量")
    private List<String> size39List;
    @ApiModelProperty(name = "尺码为40的数量")
    private List<String> size40List;
    @ApiModelProperty(name = "尺码为41的数量")
    private List<String> size41List;
    @ApiModelProperty(name = "尺码为42的数量")
    private List<String> size42List;
    @ApiModelProperty(name = "尺码为43的数量")
    private List<String> size43List;

}
