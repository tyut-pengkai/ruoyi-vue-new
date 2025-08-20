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
@ApiModel("档口返回基本数据")
@Data
@Accessors(chain = true)
public class StoreSimpleResDTO {

    @ApiModelProperty(value = "档口模板ID")
    private Integer templateNum;
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
    @ApiModelProperty(value = "法人代表")
    private String legalName;
    @ApiModelProperty(value = "用户营业执照名称")
    private String licenseName;
    @ApiModelProperty(value = "是否关注")
    private Boolean focus;
    @ApiModelProperty(value = "档口LOGO ID")
    private Long storeLogoId;
    @ApiModelProperty(value = "档口logo")
    private SSFileDTO logo;

    @Data
    public static class SSFileDTO {
        @ApiModelProperty(value = "文件名称")
        private String fileName;
        @ApiModelProperty(value = "文件路径")
        private String fileUrl;
        @ApiModelProperty(value = "文件类型")
        private Integer fileType;
        @ApiModelProperty(value = "文件大小")
        private BigDecimal fileSize;
    }

}
