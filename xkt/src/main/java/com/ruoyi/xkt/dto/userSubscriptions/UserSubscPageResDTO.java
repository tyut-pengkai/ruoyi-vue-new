package com.ruoyi.xkt.dto.userSubscriptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("电商卖家关注档口列表返回数据")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
public class UserSubscPageResDTO {

    @ApiModelProperty(value = "用户关注档口ID")
    private Long userSubscId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(name = "档口名称")
    private String storeName;
    @ApiModelProperty(name = "联系电话")
    private String contactPhone;
    @ApiModelProperty(name = "备选联系电话")
    private String contactBackPhone;
    @ApiModelProperty(name = "微信账号")
    private String wechatAccount;
    @ApiModelProperty(name = "QQ账号")
    private String qqAccount;
    @ApiModelProperty(name = "档口地址")
    private String storeAddress;

}