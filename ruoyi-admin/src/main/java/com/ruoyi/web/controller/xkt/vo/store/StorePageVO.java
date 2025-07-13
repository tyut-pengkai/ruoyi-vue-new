package com.ruoyi.web.controller.xkt.vo.store;

import com.ruoyi.web.controller.xkt.vo.BasePageVO;
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
@Data
@ApiModel
public class StorePageVO extends BasePageVO {

    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "档口状态")
    private Integer storeStatus;

}
