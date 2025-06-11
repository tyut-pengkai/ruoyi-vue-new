package com.ruoyi.xkt.dto.storeProductStock;

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
@ApiModel("档口库存导出数据")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreProdStockExportDTO {

    @ApiModelProperty(value = "storeId")
    private Long storeId;
    @ApiModelProperty(value = "storeProdStockIdList")
    private List<Long> storeProdStockIdList;
    @ApiModelProperty(value = "是否全量导出")
    private Boolean fullExport;

}
