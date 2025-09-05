package com.ruoyi.xkt.dto.storeProdColorSize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
@Accessors(chain = true)
public class StoreUnsetSnDTO {

    /**
     * 未设置条码商品列表
     */
    @ApiModelProperty(value = "未设置条码商品列表")
    List<String> unsetSnList;

}
