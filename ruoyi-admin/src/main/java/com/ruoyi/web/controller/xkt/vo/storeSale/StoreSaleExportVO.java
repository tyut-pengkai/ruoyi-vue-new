package com.ruoyi.web.controller.xkt.vo.storeSale;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@ApiModel(value = "导出销售出库")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreSaleExportVO {

    @NotNull(message = "storeId不能为空!")
    @ApiModelProperty(value = "storeId", required = true)
    private Long storeId;
    @ApiModelProperty(value = "storeSaleIdList")
    private List<Long> storeSaleIdList;
    @ApiModelProperty(value = "导出开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date voucherDateStart;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "导出结束时间")
    private Date voucherDateEnd;

}
