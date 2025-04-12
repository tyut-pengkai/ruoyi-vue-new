package com.ruoyi.xkt.dto.userSubscriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.xkt.dto.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户关注档口列表查询入参")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSubscPageDTO extends BasePageDTO {

    @ApiModelProperty(value = "档口名称")
    private String storeName;

}
