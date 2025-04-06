package com.ruoyi.xkt.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("创建档口")
@Data
@Accessors(chain = true)
public class StoreCreateDTO {

    @ApiModelProperty(value = "档口绑定的用户")
    private Long userId;

}
