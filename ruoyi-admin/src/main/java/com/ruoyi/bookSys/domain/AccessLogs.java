package com.ruoyi.bookSys.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 通行记录对象 access_logs
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
public class AccessLogs extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日志ID */
    private Long logId;

    /** 访客记录ID */
    @Excel(name = "访客记录ID")
    private Long recordId;

    /** 门禁编号 */
    @Excel(name = "门禁编号")
    private Long gateId;

    /** 门禁名称 */
    @Excel(name = "门禁名称")
    private String gateName;

    /** 通行类型(1:进入/2:离开) */
    @Excel(name = "通行类型(1:进入/2:离开)")
    private Integer accessType;

    /** 通行时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "通行时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date accessTime;

    /** 验证方式(1:二维码/2:刷脸/3:人工/4:刷卡) */
    @Excel(name = "验证方式(1:二维码/2:刷脸/3:人工/4:刷卡)")
    private Integer verifyMethod;

    /** 验证结果 */
    @Excel(name = "验证结果")
    private Integer verifyResult;

    /** 验证失败原因 */
    @Excel(name = "验证失败原因")
    private String failReason;

    /** 抓拍照片路径 */
    @Excel(name = "抓拍照片路径")
    private String photoPath;

    /** 体温(可选) */
    @Excel(name = "体温(可选)")
    private BigDecimal temperature;

    public void setLogId(Long logId) 
    {
        this.logId = logId;
    }

    public Long getLogId() 
    {
        return logId;
    }

    public void setRecordId(Long recordId) 
    {
        this.recordId = recordId;
    }

    public Long getRecordId() 
    {
        return recordId;
    }

    public void setGateId(Long gateId) 
    {
        this.gateId = gateId;
    }

    public Long getGateId() 
    {
        return gateId;
    }

    public void setGateName(String gateName) 
    {
        this.gateName = gateName;
    }

    public String getGateName() 
    {
        return gateName;
    }

    public void setAccessType(Integer accessType) 
    {
        this.accessType = accessType;
    }

    public Integer getAccessType() 
    {
        return accessType;
    }

    public void setAccessTime(Date accessTime) 
    {
        this.accessTime = accessTime;
    }

    public Date getAccessTime() 
    {
        return accessTime;
    }

    public void setVerifyMethod(Integer verifyMethod) 
    {
        this.verifyMethod = verifyMethod;
    }

    public Integer getVerifyMethod() 
    {
        return verifyMethod;
    }

    public void setVerifyResult(Integer verifyResult) 
    {
        this.verifyResult = verifyResult;
    }

    public Integer getVerifyResult() 
    {
        return verifyResult;
    }

    public void setFailReason(String failReason) 
    {
        this.failReason = failReason;
    }

    public String getFailReason() 
    {
        return failReason;
    }

    public void setPhotoPath(String photoPath) 
    {
        this.photoPath = photoPath;
    }

    public String getPhotoPath() 
    {
        return photoPath;
    }

    public void setTemperature(BigDecimal temperature) 
    {
        this.temperature = temperature;
    }

    public BigDecimal getTemperature() 
    {
        return temperature;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logId", getLogId())
            .append("recordId", getRecordId())
            .append("gateId", getGateId())
            .append("gateName", getGateName())
            .append("accessType", getAccessType())
            .append("accessTime", getAccessTime())
            .append("verifyMethod", getVerifyMethod())
            .append("verifyResult", getVerifyResult())
            .append("failReason", getFailReason())
            .append("photoPath", getPhotoPath())
            .append("temperature", getTemperature())
            .toString();
    }
}
