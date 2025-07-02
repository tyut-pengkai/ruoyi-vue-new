package com.ruoyi.xkt.dto.storeFactory;

import com.ruoyi.xkt.dto.BasePageDTO;
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
@Data
public class StoreFactoryPageDTO extends BasePageDTO {

    @ApiModelProperty(value = "工厂名称")
    private String facName;
    @ApiModelProperty(value = "档口ID")
    @NotNull(message = "档口ID不能为空")
    private Long storeId;

}
