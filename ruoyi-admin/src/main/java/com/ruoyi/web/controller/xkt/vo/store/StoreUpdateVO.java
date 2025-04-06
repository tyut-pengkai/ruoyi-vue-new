package com.ruoyi.web.controller.xkt.vo.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("更改档口基本信息")
@Data
@Accessors(chain = true)
public class StoreUpdateVO {

    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空")
    private Long storeId;
    @ApiModelProperty(value = "档口名称", required = true)
    @NotBlank(message = "档口名称不能为空")
    private String storeName;
    @ApiModelProperty(value = "品牌名称", required = true)
    @NotBlank(message = "品牌名称不能为空")
    private String brandName;
    @ApiModelProperty(value = "联系人", required = true)
    @NotBlank(message = "联系人不能为空")
    private String contactName;
    @ApiModelProperty(value = "联系电话", required = true)
    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式不正确，请输入有效的中国大陆手机号")
    private String contactPhone;
    @ApiModelProperty(value = "备选联系电话")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "备选联系电话格式不正确，请输入有效的中国大陆手机号")
    private String contactBackPhone;
    @ApiModelProperty(value = "微信账号")
    private String wechatAccount;
    @ApiModelProperty(value = "QQ账号")
    private String qqAccount;
    @ApiModelProperty(value = "支付宝账号")
    private String alipayAccount;
    @ApiModelProperty(value = "经营年限")
    private Integer operateYears;
    @ApiModelProperty(name = "档口地址")
    private String storeAddress;
    @ApiModelProperty(name = "工厂地址")
    private String facAddress;
    @ApiModelProperty(name = "生产规模")
    private Integer prodScale;

}
