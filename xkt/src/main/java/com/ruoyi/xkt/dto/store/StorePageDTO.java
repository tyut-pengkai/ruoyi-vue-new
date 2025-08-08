package com.ruoyi.xkt.dto.store;

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
@ApiModel("档口分页查询入参")
@Data
public class StorePageDTO extends BasePageDTO {

    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "启用停用 0启用 2停用")
    private String delFlag;
    @ApiModelProperty(value = "档口状态")
    private Integer storeStatus;

}
