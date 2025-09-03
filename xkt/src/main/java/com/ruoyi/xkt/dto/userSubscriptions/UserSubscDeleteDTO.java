package com.ruoyi.xkt.dto.userSubscriptions;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data

public class UserSubscDeleteDTO {

    @ApiModelProperty(value = "档口ID列表")
    private List<Long> storeIdList;

}
