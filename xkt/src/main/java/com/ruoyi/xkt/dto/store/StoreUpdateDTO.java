package com.ruoyi.xkt.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("更改档口基本信息")
@Data
@Accessors(chain = true)
public class StoreUpdateDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "联系人")
    private String contactName;
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;
    @ApiModelProperty(value = "备选联系电话")
    private String contactBackPhone;
    @ApiModelProperty(value = "微信账号")
    private String wechatAccount;
    @ApiModelProperty(value = "QQ账号")
    private String qqAccount;
    @ApiModelProperty(value = "支付宝账号")
    private String alipayAccount;
    @ApiModelProperty(value = "经营年限")
    private Integer operateYears;
    @ApiModelProperty(value = "档口地址")
    private String storeAddress;
    @ApiModelProperty(value = "工厂地址")
    private String facAddress;
    @ApiModelProperty(value = "生产规模")
    private Integer prodScale;
    @ApiModelProperty(value = "档口logo")
    private SUFileDTO storeLogo;


    @Data
    public static class SUFileDTO {
        @ApiModelProperty(value = "文件名称")
        private String fileName;
        @ApiModelProperty(value = "文件路径")
        private String fileUrl;
        @ApiModelProperty(value = "文件类型（6档口营业执照）")
        private Integer fileType;
        @ApiModelProperty(value = "文件大小")
        private BigDecimal fileSize;
    }

}
