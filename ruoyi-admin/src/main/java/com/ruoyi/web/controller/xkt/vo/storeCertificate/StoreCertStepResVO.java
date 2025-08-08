package com.ruoyi.web.controller.xkt.vo.storeCertificate;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口认证详情")
@Data
@Accessors(chain = true)
public class StoreCertStepResVO {

    @ApiModelProperty(value = "档口认证信息")
    private SCSStoreCertVO storeCert;
    @ApiModelProperty(value = "档口基础信息")
    private SCSStoreBasicVO storeBasic;

    @Data
    @ApiModel
    public static class SCSStoreBasicVO {
        @ApiModelProperty(value = "档口名称")
        private String storeName;
        @ApiModelProperty(value = "档口LOGO")
        private StoreCertCreateVO.SCStoreFileVO storeLogo;
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
        @ApiModelProperty(value = "档口地址")
        @Size(min = 0, max = 50, message = "档口地址长度必须在0到50个字之间")
        private String storeAddress;
        @ApiModelProperty(value = "工厂地址")
        @Size(min = 0, max = 50, message = "工厂地址长度必须在0到50个字之间")
        private String facAddress;
        @ApiModelProperty(value = "生产规模")
        private Integer prodScale;
        @ApiModelProperty(value = "拒绝理由")
        private String rejectReason;
    }

    @Data
    public static class SCSStoreCertVO {
        @ApiModelProperty(value = "档口认证ID")
        private Long storeCertId;
        @ApiModelProperty(value = "档口ID")
        private Long storeId;
        @ApiModelProperty(value = "真实姓名")
        private String realName;
        @ApiModelProperty(value = "联系电话")
        private String phone;
        @ApiModelProperty(value = "身份证号")
        private String idCard;
        @ApiModelProperty(value = "档口认证文件")
        List<StoreCertFileVO> fileList;
        @ApiModelProperty(value = "统一社会信用代码")
        private String socialCreditCode;
        @ApiModelProperty(value = "经营类型")
        private Integer soleProprietorshipType;
        @ApiModelProperty(value = "营业执照名称")
        private String licenseName;
        @ApiModelProperty(value = "市场主体类型")
        private Integer marketEntryType;
        @ApiModelProperty(value = "登记机关")
        private String registerOrg;
        @ApiModelProperty(value = "登记状态")
        private Integer registerStatus;
        @ApiModelProperty(value = "法定代表人/负责人名称")
        private String legalName;
        @ApiModelProperty(value = "注册资本(万)")
        private Integer registerCapital;
        @ApiModelProperty(value = "实际经营地址")
        private String realBusinessAddress;
        @ApiModelProperty(value = "经营范围")
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
    public static class StoreCertFileVO {
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
