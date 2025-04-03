package com.ruoyi.xkt.dto.storeFactory;

import com.ruoyi.xkt.dto.BasePageDTO;
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
@ApiModel("档口工厂分页查询入参")
@Data
public class StoreFactoryPageDTO extends BasePageDTO {

    @ApiModelProperty(name = "工厂名称")
    private String facName;
    @ApiModelProperty(name = "档口ID")
    @NotNull(message = "档口ID不能为空")
    private Long storeId;

}
