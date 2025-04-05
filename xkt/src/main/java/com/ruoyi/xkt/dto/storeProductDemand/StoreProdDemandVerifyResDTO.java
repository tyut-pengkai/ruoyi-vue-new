package com.ruoyi.xkt.dto.storeProductDemand;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品生产入库校验是否存在需求单返回前端数据")
@Data
public class StoreProdDemandVerifyResDTO {

    @ApiModelProperty(value = "需求不存在信息列表")
    private List<String> errorMsgList;

}
