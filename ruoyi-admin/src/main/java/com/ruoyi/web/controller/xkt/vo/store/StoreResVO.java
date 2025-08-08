package com.ruoyi.web.controller.xkt.vo.store;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
public class StoreResVO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "登录账号")
    private String loginAccount;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "档口负责人ID")
    private Long userId;
    @ApiModelProperty(value = "档口logo")
    private SFileVO storeLogo;
    @ApiModelProperty(value = "档口负责人ID")
    private String userName;
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "试用截止时间")
    private Date trialEndTime;
    @ApiModelProperty(value = "已使用文件大小")
    private BigDecimal storageUsage;
    @ApiModelProperty(value = "档口状态")
    private Integer storeStatus;
    @ApiModelProperty(value = "拒绝理由")
    private String rejectReason;
    @ApiModelProperty(value = "档口模板ID")
    private Integer templateNum;

    @Data
    public static class SFileVO {
        @ApiModelProperty(value = "文件名称")
        private String fileName;
        @ApiModelProperty(value = "文件路径")
        private String fileUrl;
        @ApiModelProperty(value = "文件类型（4 人脸照片  5 国徽照片  6档口营业执照）")
        private Integer fileType;
        @ApiModelProperty(value = "文件大小")
        private BigDecimal fileSize;
    }


}
