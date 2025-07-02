package com.ruoyi.xkt.dto.storeCertificate;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

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

    @ApiModelProperty(value = "档口ID")
    private Long storeCertId;
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    @ApiModelProperty(value = "真实姓名", required = true)
    private String realName;
    @ApiModelProperty(value = "联系电话", required = true)
    private String phone;
    @ApiModelProperty(value = "身份证号", required = true)
    private String idCard;
    @ApiModelProperty(value = "认证文件列表", required = true)
    private List<StoreCertFileDTO> fileList;
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

    @Data
    @ApiModel(value = "档口文件")
    public static class StoreCertFileDTO {
        @ApiModelProperty(value = "文件名称", required = true)
        private String fileName;
        @ApiModelProperty(value = "文件路径", required = true)
        private String fileUrl;
        @ApiModelProperty(value = "文件类型（4 人脸照片  5 国徽照片  6档口营业执照）")
        private Integer fileType;
        @ApiModelProperty(value = "文件大小", required = true)
        private BigDecimal fileSize;
    }

}
