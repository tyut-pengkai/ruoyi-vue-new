package com.ruoyi.web.controller.xkt.vo.storeProd;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("一键迁移条码时输入货号查询列表")
@Data
@Builder
@Accessors(chain = true)
public class StoreProdFuzzyResVO {

    @ApiModelProperty("档口商品ID")
    private Long storeProdId;
    @ApiModelProperty("档口ID")
    private Long storeId;
    @ApiModelProperty(name = "商品货号")
    private String prodArtNum;
    @ApiModelProperty("商品下颜色列表")
    private List<StoreProdFuzzyColorResVO> colorList;

    @Data
    public static class StoreProdFuzzyColorResVO {
        @ApiModelProperty("档口颜色ID")
        private Long storeColorId;
        @ApiModelProperty("颜色名称")
        private String colorName;
    }
}
