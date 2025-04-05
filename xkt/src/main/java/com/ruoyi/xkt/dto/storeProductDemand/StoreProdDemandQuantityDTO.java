package com.ruoyi.xkt.dto.storeProductDemand;

import com.ruoyi.common.annotation.Excel;
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
    private List<String> size30List;
    @ApiModelProperty(value = "尺码为31的数量")
    private List<String> size31List;
    @ApiModelProperty(value = "尺码为32的数量")
    private List<String> size32List;
    @ApiModelProperty(value = "尺码为33的数量")
    private List<String> size33List;
    @ApiModelProperty(value = "尺码为34的数量")
    private List<String> size34List;
    @ApiModelProperty(value = "尺码为35的数量")
    private List<String> size35List;
    @ApiModelProperty(value = "尺码为36的数量")
    private List<String> size36List;
    @ApiModelProperty(value = "尺码为37的数量")
    private List<String> size37List;
    @ApiModelProperty(value = "尺码为38的数量")
    private List<String> size38List;
    @ApiModelProperty(value = "尺码为39的数量")
    private List<String> size39List;
    @ApiModelProperty(value = "尺码为40的数量")
    private List<String> size40List;
    @ApiModelProperty(value = "尺码为41的数量")
    private List<String> size41List;
    @ApiModelProperty(value = "尺码为42的数量")
    private List<String> size42List;
    @ApiModelProperty(value = "尺码为43的数量")
    private List<String> size43List;

}
