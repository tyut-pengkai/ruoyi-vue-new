package com.ruoyi.web.controller.xkt.vo.userSubscriptions;

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
@ApiModel("电商卖家取消店铺关注")
@Data

public class UserSubscDeleteVO {

    @NotNull(message = "档口ID列表不能为空!")
    @ApiModelProperty(value = "档口ID列表", required = true)
    private List<Long> storeIdList;

}
