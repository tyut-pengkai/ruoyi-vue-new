package com.ruoyi.web.controller.xkt.vo.storeCertificate;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
@Accessors(chain = true)
public class StoreCertCreateVO {

    @ApiModelProperty(value = "档口认证ID（新增时不传，编辑必传）")
    private Long storeCertId;
    @Valid
    @ApiModelProperty(value = "档口认证信息", required = true)
    @NotNull(message = "档口认证信息不能为空!")
    private SCStoreCertVO storeCert;
    @Valid
    @ApiModelProperty(value = "档口基础信息", required = true)
    @NotNull(message = "档口基础信息不能为空!")
    private SCStoreBasicVO storeBasic;

    @Data
    @ApiModel
    public static class SCStoreBasicVO {
        @NotBlank(message = "档口名称不能为空!")
        @ApiModelProperty(value = "档口名称")
        @Size(min = 0, max = 50, message = "档口名称长度必须在0到10个字之间")
        private String storeName;
        @ApiModelProperty(value = "档口LOGO")
        private SCStoreFileVO storeLogo;
        @NotBlank(message = "联系人不能为空!")
        @ApiModelProperty(value = "联系人", required = true)
        private String contactName;
        @NotBlank(message = "联系电话不能为空!")
        @ApiModelProperty(value = "联系电话", required = true)
        private String contactPhone;
        @ApiModelProperty(value = "备选联系电话")
        private String contactBackPhone;
        @ApiModelProperty(value = "微信账号")
        private String wechatAccount;
        @ApiModelProperty(value = "QQ账号")
        private String qqAccount;
        @ApiModelProperty(value = "档口地址")
        @Size(min = 0, max = 50, message = "档口地址长度必须在0到50个字之间")
        private String storeAddress;
        @ApiModelProperty(value = "工厂地址")
        @Size(min = 0, max = 50, message = "工厂地址长度必须在0到50个字之间")
        private String facAddress;
        @ApiModelProperty(value = "生产规模")
        private Integer prodScale;
        @ApiModelProperty(value = "库存系统 1步橘 2天友 3发货宝")
        private Integer stockSys;
    }

    @Data
    @ApiModel
    public static class SCStoreCertVO {
        @ApiModelProperty(value = "真实姓名", required = true)
        @NotBlank(message = "真实姓名不能为空")
        @Size(min = 0, max = 30, message = "真实姓名长度必须在1到30个字之间")
        private String realName;
        @ApiModelProperty(value = "联系电话", required = true)
        @NotBlank(message = "联系电话不能为空")
        @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式不正确，请输入有效的中国大陆手机号")
        private String phone;
        @ApiModelProperty(value = "身份证号", required = true)
        @NotBlank(message = "身份证号不能为空")
        @Pattern(regexp = "(^\\d{15}$)|(^\\d{17}([0-9]|X|x)$)", message = "身份证号格式不正确，请输入有效的15位或18位身份证号")
        private String idCard;
        @Valid
        @ApiModelProperty(value = "认证文件列表", required = true)
        @NotNull(message = "认证文件列表不能为空")
        private List<SCStoreFileVO> fileList;
        @ApiModelProperty(value = "统一社会信用代码", required = true)
        @NotBlank(message = "统一社会信用代码不能为空")
        @Pattern(regexp = "^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$",
                message = "统一社会信用代码格式不正确，请输入有效的18位代码")
        private String socialCreditCode;
        @ApiModelProperty(value = "类型", required = true)
        @NotNull(message = "类型不能为空")
        private Integer soleProprietorshipType;
        @ApiModelProperty(value = "营业执照名称", required = true)
        @NotBlank(message = "营业执照名称不能为空")
        @Size(min = 0, max = 100, message = "营业执照名称长度必须在1到100个字之间")
        private String licenseName;
        @ApiModelProperty(value = "市场主体类型", required = true)
        @NotNull(message = "市场主体类型不能为空")
        private Integer marketEntryType;
        @ApiModelProperty(value = "登记机关", required = true)
        @NotBlank(message = "登记机关不能为空")
        @Size(min = 0, max = 100, message = "登记机关长度必须在1到100个字之间")
        private String registerOrg;
        @ApiModelProperty(value = "登记状态", required = true)
        private Integer registerStatus;
        @ApiModelProperty(value = "法定代表人/负责人名称", required = true)
        @NotBlank(message = "法定代表人/负责人名称不能为空")
        @Size(min = 0, max = 30, message = "法定代表人/负责人名称长度必须在1到30个字之间")
        private String legalName;
        @ApiModelProperty(value = "注册资本(万)")
        private Integer registerCapital;
        @ApiModelProperty(value = "实际经营地址", required = true)
        @NotBlank(message = "实际经营地址不能为空")
        @Size(min = 0, max = 100, message = "实际经营地址长度必须在1到100个字之间")
        private String realBusinessAddress;
        @ApiModelProperty(value = "经营范围")
        @Size(min = 0, max = 500, message = "经营范围长度必须在1到500个字之间")
        private String businessScope;
        @JsonFormat(pattern = "yyyy-MM-dd")
        @ApiModelProperty(value = "营业期限开始时间")
        private Date businessTermStartDate;
        @JsonFormat(pattern = "yyyy-MM-dd")
        @ApiModelProperty(value = "营业期限截止时间")
        private Date businessTermEndDate;
        @JsonFormat(pattern = "yyyy-MM-dd")
        @ApiModelProperty(value = "成立日期")
        private Date establishDate;
        @JsonFormat(pattern = "yyyy-MM-dd")
        @ApiModelProperty(value = "核准日期")
        private Date approvalDate;
    }


    @Data
    public static class SCStoreFileVO {
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
