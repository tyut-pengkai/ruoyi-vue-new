package com.ruoyi.xkt.dto.storeSale;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口销售导出数据")
@Data

public class StoreSaleExportDTO {

    @ApiModelProperty(value = "storeId")
    private Long storeId;
    @ApiModelProperty(value = "storeSaleIdList")
    private List<Long> storeSaleIdList;
    @ApiModelProperty(value = "导出开始时间")
    private Date voucherDateStart;
    @ApiModelProperty(value = "导出结束时间")
    private Date voucherDateEnd;

}
