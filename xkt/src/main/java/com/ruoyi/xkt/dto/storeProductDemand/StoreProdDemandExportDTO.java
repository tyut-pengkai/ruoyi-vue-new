package com.ruoyi.xkt.dto.storeProductDemand;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data

public class StoreProdDemandExportDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "需求状态 1 待生产 2 生产中 3 生产完成")
    private Integer detailStatus;
    @ApiModelProperty(value = "档口需求明细ID")
    private List<Long> storeProdDemandDetailIdList;
    @ApiModelProperty(value = "导出开始时间")
    private Date voucherDateStart;
    @ApiModelProperty(value = "导出结束时间")
    private Date voucherDateEnd;

}
