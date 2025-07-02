package com.ruoyi.xkt.dto.storeSale;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreSalePayStatusDTO {

    @ApiModelProperty(value = "结算的storeSaleId列表")
    private List<Long> storeSaleIdList;

}
