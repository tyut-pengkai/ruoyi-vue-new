package com.ruoyi.xkt.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 档口认证对象 store_certificate
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreCertificate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口认证ID
     */
    private Long storeCertId;

    /**
     * store.id
     */
    @Excel(name = "store.id")
    private Long storeId;

    /**
     * 真实姓名
     */
    @Excel(name = "真实姓名")
    private String realName;

    /**
     * 联系电话
     */
    @Excel(name = "联系电话")
    private String phone;

    /**
     * 身份证号
     */
    @Excel(name = "身份证号")
    private String idCard;

    /**
     * 身份证人脸文件ID
     */
    @Excel(name = "身份证人脸文件ID")
    private Long idCardFrontFileId;

    /**
     * 身份证国徽文件ID
     */
    @Excel(name = "身份证国徽文件ID")
    private Long idCardBackFileId;

    /**
     * 营业执照文件ID
     */
    @Excel(name = "营业执照文件ID")
    private Long licenseFileId;

    /**
     * 统一社会信用代码
     */
    @Excel(name = "统一社会信用代码")
    private String socialCreditCode;

    /**
     * 经营类型
     */
    @Excel(name = "经营类型")
    private Long soleProprietorshipType;

    /**
     * 市场主体类型
     */
    @Excel(name = "市场主体类型")
    private Long marketEntryType;

    /**
     * 营业执照名称
     */
    @Excel(name = "营业执照名称")
    private String licenseName;

    /**
     * 登记机关
     */
    @Excel(name = "登记机关")
    private String registerOrg;

    /**
     * 登记状态
     */
    @Excel(name = "登记状态")
    private String registerStatus;

    /**
     * 法定代表人/负责人名称
     */
    @Excel(name = "法定代表人/负责人名称")
    private String legalName;

    /**
     * 实际经营地址
     */
    @Excel(name = "实际经营地址")
    private String realBusinessAddress;

    /**
     * 经营范围
     */
    @Excel(name = "经营范围")
    private String businessScope;

    /**
     * 注册资本(万)
     */
    @Excel(name = "注册资本(万)")
    private Integer registerCapital;

    /**
     * 成立日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "成立日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date establishDate;

    /**
     * 营业期限开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "营业期限开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date businessTermStartDate;

    /**
     * 营业期限截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "营业期限截止时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date businessTermEndDate;

    /**
     * 核准日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "核准日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date approvalDate;

    /**
     * 档口认证状态
     */
    @Excel(name = "档口认证状态")
    private String certStatus;

    /**
     * 版本号
     */
    @Excel(name = "版本号")
    private Long version;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("storeCertId", getStoreCertId())
                .append("storeId", getStoreId())
                .append("realName", getRealName())
                .append("phone", getPhone())
                .append("idCard", getIdCard())
                .append("idCardFrontFileId", getIdCardFrontFileId())
                .append("idCardBackFileId", getIdCardBackFileId())
                .append("licenseFileId", getLicenseFileId())
                .append("socialCreditCode", getSocialCreditCode())
                .append("soleProprietorshipType", getSoleProprietorshipType())
                .append("marketEntryType", getMarketEntryType())
                .append("licenseName", getLicenseName())
                .append("registerOrg", getRegisterOrg())
                .append("registerStatus", getRegisterStatus())
                .append("legalName", getLegalName())
                .append("realBusinessAddress", getRealBusinessAddress())
                .append("businessScope", getBusinessScope())
                .append("registerCapital", getRegisterCapital())
                .append("establishDate", getEstablishDate())
                .append("businessTermStartDate", getBusinessTermStartDate())
                .append("businessTermEndDate", getBusinessTermEndDate())
                .append("approvalDate", getApprovalDate())
                .append("certStatus", getCertStatus())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
