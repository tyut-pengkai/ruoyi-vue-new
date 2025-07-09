package com.ruoyi.xkt.dto.storeProdColorPrice;

import com.ruoyi.system.domain.dto.BasePageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreProdColorPricePageDTO extends BasePageDTO {

    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    @ApiModelProperty(value = "商品状态[1未发布，2在售，3尾货，4已下架，5已删除]", required = true)
    private List<Integer> prodStatusList;


}
