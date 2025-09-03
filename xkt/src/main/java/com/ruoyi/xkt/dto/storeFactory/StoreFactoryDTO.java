package com.ruoyi.xkt.dto.storeFactory;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data

public class StoreFactoryDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口工厂ID")
    private Long storeFactoryId;
    @ApiModelProperty(value = "工厂名称")
    private String facName;
    @ApiModelProperty(value = "工厂地址")
    private String facAddress;
    @ApiModelProperty(value = "联系人")
    private String facContact;
    @ApiModelProperty(value = "工厂联系电话")
    private String facPhone;
    @ApiModelProperty("备注")
    private String remark;

}
