package com.ruoyi.web.controller.xkt.vo.userSubscriptions;

import com.ruoyi.web.controller.xkt.vo.BasePageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户关注档口列表查询入参")
@Data

public class UserSubscPageVO extends BasePageVO {

    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @NotNull(message = "查询来源不能为空!")
    @ApiModelProperty(value = "查询来源", notes = "1 PC, 2 APP")
    private Integer source;

}
