package com.ruoyi.web.controller.xkt.vo.advertRound.pc;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("PC 首页 风格榜")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PCIndexMidStyleVO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "1推广图")
    private Integer displayType;
    @ApiModelProperty(value = "推广图路径")
    private String picUrl;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;
    @ApiModelProperty(value = "风格榜列表")
    private List<PCIMSStyleVO> saleList;

    @Data
    @ApiModel(value = "风格榜列表")
    public static class PCIMSStyleVO {
        @ApiModelProperty(value = "2商品")
        private Integer displayType;
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "商品货号")
        private String prodArtNum;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "售价")
        private BigDecimal price;
        @ApiModelProperty(value = "商品第一张主图路径")
        private String manPicUrl;
    }

}
