package com.ruoyi.xkt.dto.storeProdStorage;

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
@ApiModel("档口入库导出数据")
@Data

public class StoreStorageExportDTO {

    @ApiModelProperty(value = "storeId")
    private Long storeId;
    @ApiModelProperty(value = "货号")
    private String prodArtNum;
    @ApiModelProperty(value = "入库类型 1 PROD_STORAGE  其它入库 2 OTHER_STORAGE  维修入库 3 REPAIR_STORAGE")
    private Integer storageType;
    @ApiModelProperty(value = "工厂名称")
    private String facName;
    @ApiModelProperty(value = "storeProdStorageId")
    private List<Long> storeProdStorageIdList;
    @ApiModelProperty(value = "导出开始时间")
    private Date voucherDateStart;
    @ApiModelProperty(value = "导出结束时间")
    private Date voucherDateEnd;

}
