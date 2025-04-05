package com.ruoyi.xkt.dto.storeSale;

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
@ApiModel("档口商品销售客户欠款结算")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreSalePayStatusDTO {

    @ApiModelProperty(value = "结算的storeSaleId列表")
    private List<Long> storeSaleIdList;

}
