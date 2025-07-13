package com.ruoyi.web.controller.xkt.vo.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口数据概览")
@Data
@Accessors(chain = true)
public class StoreOverviewVO {

    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空")
    private Long storeId;
    @ApiModelProperty(value = "查询开始时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull(message = "查询开始时间不能为空")
    private Date voucherDateStart;
    @ApiModelProperty(value = "查询结束时间", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @NotNull(message = "查询结束时间不能为空")
    private Date voucherDateEnd;

}
