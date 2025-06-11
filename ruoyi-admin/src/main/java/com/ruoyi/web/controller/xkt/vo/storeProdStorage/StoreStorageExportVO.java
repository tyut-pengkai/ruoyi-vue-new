package com.ruoyi.web.controller.xkt.vo.storeProdStorage;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口入库导出数据")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreStorageExportVO {

    @NotNull(message = "storeId不能为空!")
    @ApiModelProperty(value = "storeId")
    private Long storeId;
    @ApiModelProperty(value = "storeProdStorageId")
    private List<Long> storeProdStorageIdList;
    @ApiModelProperty(value = "导出开始时间")
    private Date voucherDateStart;
    @ApiModelProperty(value = "导出结束时间")
    private Date voucherDateEnd;

}
