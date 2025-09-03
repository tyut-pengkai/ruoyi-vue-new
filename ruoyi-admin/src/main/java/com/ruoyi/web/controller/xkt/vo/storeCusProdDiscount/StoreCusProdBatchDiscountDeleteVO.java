package com.ruoyi.web.controller.xkt.vo.storeCusProdDiscount;

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
@ApiModel("档口客户批量删除")
@Data

public class StoreCusProdBatchDiscountDeleteVO {

    @NotNull(message = "ID列表不能为空!")
    @ApiModelProperty(value = "ID列表", required = true)
    private List<Long> storeCusProdDiscIdList;

}
