package com.ruoyi.xkt.dto.storeProdColorSize;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel(value = "库存条码查询model")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreStockTakingSnDTO {

    @ApiModelProperty(value = "档口ID")
    private String storeId;
    @ApiModelProperty(value = "条码列表")
    private List<String> snList;

}
