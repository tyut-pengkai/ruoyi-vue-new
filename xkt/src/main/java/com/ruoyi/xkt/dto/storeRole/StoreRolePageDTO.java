package com.ruoyi.xkt.dto.storeRole;

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
@ApiModel("档口子角色分页查询入参")
@Data
public class StoreRolePageDTO extends BasePageDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;

}
