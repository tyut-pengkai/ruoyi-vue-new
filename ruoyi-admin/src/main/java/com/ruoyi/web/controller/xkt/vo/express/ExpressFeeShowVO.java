package com.ruoyi.web.controller.xkt.vo.express;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-06-11
 */
@ApiModel
@Data
public class ExpressFeeShowVO {

    @ApiModelProperty(value = "快递名称")
    private String expressName;

    @ApiModelProperty(value = "列表项")
    private List<Item> items;

    @ApiModel
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {

        @ApiModelProperty(value = "可配送区域")
        private String ares;

        @ApiModelProperty(value = "首件")
        private Integer firstItemCount;

        @ApiModelProperty(value = "运费")
        private BigDecimal firstItemAmount;

        @ApiModelProperty(value = "续件")
        private Integer nextItemCount;

        @ApiModelProperty(value = "续费")
        private BigDecimal nextItemAmount;
    }
}
