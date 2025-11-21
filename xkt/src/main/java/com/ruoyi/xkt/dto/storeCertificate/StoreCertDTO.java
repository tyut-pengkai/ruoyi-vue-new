package com.ruoyi.xkt.dto.storeCertificate;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Accessors(chain = true)
public class StoreCertDTO {

    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    @ApiModelProperty(value = "档口认证ID（新增时不传，编辑必传）")
    private Long storeCertId;
    @ApiModelProperty(value = "档口认证信息", required = true)
    private SCStoreCertDTO storeCert;
    @ApiModelProperty(value = "档口基础信息", required = true)
    private SCStoreBasicDTO storeBasic;

    @Data
    public static class SCStoreBasicDTO {
        @ApiModelProperty(value = "档口名称")
        private String storeName;
        @ApiModelProperty(value = "档口LOGO")
        private SCStoreFileDTO storeLogo;
        @ApiModelProperty(value = "联系人", required = true)
        private String contactName;
        @ApiModelProperty(value = "联系电话", required = true)
        private String contactPhone;
        @ApiModelProperty(value = "备选联系电话")
        private String contactBackPhone;
        @ApiModelProperty(value = "微信账号")
        private String wechatAccount;
        @ApiModelProperty(value = "QQ账号")
        private String qqAccount;
        @ApiModelProperty(value = "档口地址")
        private String storeAddress;
        @ApiModelProperty(value = "工厂地址")
        private String facAddress;
        @ApiModelProperty(value = "生产规模")
        private Integer prodScale;
    }

    @Data
    public static class SCStoreCertDTO {
        @ApiModelProperty(value = "法人真实姓名", required = true)
        private String realName;
        @ApiModelProperty(value = "法人联系电话", required = true)
        private String phone;
        @ApiModelProperty(value = "法人身份证号", required = true)
        private String idCard;
        @ApiModelProperty(value = "短信验证码")
        private String code;
        @ApiModelProperty(value = "认证文件列表", required = true)
        private List<SCStoreFileDTO> fileList;
        @ApiModelProperty(value = "统一社会信用代码", required = true)
        private String socialCreditCode;
        @ApiModelProperty(value = "经营类型", required = true)
        private Integer soleProprietorshipType;
        @ApiModelProperty(value = "营业执照名称", required = true)
        private String licenseName;
        @ApiModelProperty(value = "市场主体类型", required = true)
        private Integer marketEntryType;
        @ApiModelProperty(value = "登记机关", required = true)
        private String registerOrg;
        @ApiModelProperty(value = "登记状态", required = true)
        private Integer registerStatus;
        @ApiModelProperty(value = "法定代表人/负责人名称", required = true)
        private String legalName;
        @ApiModelProperty(value = "注册资本(万)")
        private Integer registerCapital;
        @ApiModelProperty(value = "实际经营地址", required = true)
        private String realBusinessAddress;
        @ApiModelProperty(value = "经营范围")
        private String businessScope;
        @ApiModelProperty(value = "营业期限开始时间")
        private Date businessTermStartDate;
        @ApiModelProperty(value = "营业期限截止时间")
        private Date businessTermEndDate;
        @ApiModelProperty(value = "成立日期")
        private Date establishDate;
        @ApiModelProperty(value = "核准日期")
        private Date approvalDate;
    }


    @Data
    public static class SCStoreFileDTO {
        @NotBlank(message = "文件名称不能为空!")
        @ApiModelProperty(value = "文件名称", required = true)
        private String fileName;
        @NotBlank(message = "文件路径不能为空!")
        @ApiModelProperty(value = "文件路径", required = true)
        private String fileUrl;
        @NotNull(message = "文件大小不能为空!")
        @ApiModelProperty(value = "文件大小", required = true)
        private BigDecimal fileSize;
        @NotNull(message = "文件类型不能为空!")
        @ApiModelProperty(value = "文件类型", required = true)
        private Integer fileType;
    }

}
