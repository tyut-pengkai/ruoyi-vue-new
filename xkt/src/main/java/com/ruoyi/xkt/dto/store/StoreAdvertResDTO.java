package com.ruoyi.xkt.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口App基本信息")
@Data
public class StoreAdvertResDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;
    @ApiModelProperty(value = "备选联系电话")
    private String contactBackPhone;
    @ApiModelProperty(name = "微信账号")
    private String wechatAccount;
    @ApiModelProperty(name = "QQ账号")
    private String qqAccount;

}
