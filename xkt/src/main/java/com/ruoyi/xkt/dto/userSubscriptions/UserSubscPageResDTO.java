package com.ruoyi.xkt.dto.userSubscriptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

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
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;
    @ApiModelProperty(value = "备选联系电话")
    private String contactBackPhone;
    @ApiModelProperty(value = "微信账号")
    private String wechatAccount;
    @ApiModelProperty(value = "QQ账号")
    private String qqAccount;
    @ApiModelProperty(value = "档口地址")
    private String storeAddress;
    @ApiModelProperty(value = "档口状态")
    private Integer storeDelFlag;
    @ApiModelProperty(value = "关注日期")
    private Long focusDays;
    @ApiModelProperty(value = "最近30天销售量")
    private Long last30DaysSaleQuantity;
    @ApiModelProperty(value = "最近7天新增商品数")
    private Long last7DaysNewProdQuantity;

}