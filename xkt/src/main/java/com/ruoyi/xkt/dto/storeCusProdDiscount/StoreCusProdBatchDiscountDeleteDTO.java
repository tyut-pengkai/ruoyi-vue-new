package com.ruoyi.xkt.dto.storeCusProdDiscount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口客户批量删除")
@Data

public class StoreCusProdBatchDiscountDeleteDTO {

    @ApiModelProperty(value = "ID列表")
    private List<Long> storeCusProdDiscIdList;

}
