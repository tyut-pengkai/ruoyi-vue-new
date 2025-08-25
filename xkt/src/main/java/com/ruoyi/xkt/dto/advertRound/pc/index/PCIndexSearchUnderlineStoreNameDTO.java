package com.ruoyi.xkt.dto.advertRound.pc.index;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Accessors(chain = true)
public class PCIndexSearchUnderlineStoreNameDTO {

    @ApiModelProperty(value = "4 档口名称")
    private Integer displayType;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "档口出的推广价格")
    private BigDecimal payPrice;

}
