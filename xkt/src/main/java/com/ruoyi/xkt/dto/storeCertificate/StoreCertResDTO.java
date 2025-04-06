package com.ruoyi.xkt.dto.storeCertificate;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("新增档口认证信息")
@Data
@Accessors(chain = true)
public class StoreCertResDTO {

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
    @ApiModelProperty(name = "档口认证文件")
    List<StoreCertFileDTO> fileList;
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
    @ApiModelProperty(name = "注册资本(万)")
    private Integer registerCapital;
    @ApiModelProperty(value = "实际经营地址")
    private String realBusinessAddress;
    @ApiModelProperty(value = "经营范围")
    private String businessScope;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "营业期限开始时间")
    private Date businessTermStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "营业期限截止时间")
    private Date businessTermEndDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "成立日期")
    private Date establishDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(name = "核准日期")
    private Date approvalDate;

    @Data
    @ApiModel(value = "档口文件")
    @Accessors(chain = true)
    public static class StoreCertFileDTO {
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
