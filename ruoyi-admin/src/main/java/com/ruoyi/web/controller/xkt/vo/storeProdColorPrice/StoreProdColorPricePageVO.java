package com.ruoyi.web.controller.xkt.vo.storeProdColorPrice;

import com.ruoyi.web.controller.xkt.vo.BasePageVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreProdColorPricePageVO extends BasePageVO {

    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空")
    private Long storeId;
    @ApiModelProperty(value = "商品状态[1未发布，2在售，3尾货，4已下架，5已删除]", required = true)
    @NotNull(message = "商品状态不能为空")
    private List<Integer> prodStatusList;

}
