package com.ruoyi.web.controller.xkt.vo.storeProductDemand;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
public class StoreProdDemandVerifyResVO {

    @ApiModelProperty(value = "需求不存在信息列表")
    private List<String> errorMsgList;

}
