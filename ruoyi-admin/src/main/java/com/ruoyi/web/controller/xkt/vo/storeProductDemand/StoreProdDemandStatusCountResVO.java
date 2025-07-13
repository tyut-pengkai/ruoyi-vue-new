package com.ruoyi.web.controller.xkt.vo.storeProductDemand;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreProdDemandStatusCountResVO {

    @ApiModelProperty(value = "待生产数量")
    private Integer unProductionNum;
    @ApiModelProperty(value = "生产中数量")
    private Integer inProductionNum;
    @ApiModelProperty(value = "生产完成数量")
    private Integer productionCompleteNum;

}
