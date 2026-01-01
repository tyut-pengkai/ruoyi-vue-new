package com.ruoyi.system.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 论文信息表 sys_paper
 * 
 * @author ruoyi
 */
public class SysPaper extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 论文主键 */
    @Excel(name = "论文主键", cellType = ColumnType.NUMERIC)
    private Long paperId;

    /** 论文标题 */
    @Excel(name = "论文标题")
    @NotBlank(message = "论文标题不能为空")
    @Size(min = 0, max = 200, message = "论文标题不能超过200个字符")
    private String paperTitle;

    /** 论文摘要 */
    @Excel(name = "论文摘要")
    @NotBlank(message = "论文摘要不能为空")
    @Size(min = 0, max = 500, message = "论文摘要不能超过500个字符")
    private String paperAbstract;

    /** 提交日期 */
    @Excel(name = "提交日期", width = 30, dateFormat = "yyyy-MM-dd")
    private String submitDate;

    /** 论文状态 */
    @Excel(name = "论文状态", readConverterExp = "0=草稿,1=已提交,2=审核中,3=已通过,4=未通过")
    private String status;

    /** 学生ID */
    private Long studentId;

    public Long getPaperId()
    {
        return paperId;
    }

    public void setPaperId(Long paperId)
    {
        this.paperId = paperId;
    }

    public String getPaperTitle()
    {
        return paperTitle;
    }

    public void setPaperTitle(String paperTitle)
    {
        this.paperTitle = paperTitle;
    }

    public String getPaperAbstract()
    {
        return paperAbstract;
    }

    public void setPaperAbstract(String paperAbstract)
    {
        this.paperAbstract = paperAbstract;
    }

    public String getSubmitDate()
    {
        return submitDate;
    }

    public void setSubmitDate(String submitDate)
    {
        this.submitDate = submitDate;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public Long getStudentId()
    {
        return studentId;
    }

    public void setStudentId(Long studentId)
    {
        this.studentId = studentId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("paperId", getPaperId())
            .append("paperTitle", getPaperTitle())
            .append("paperAbstract", getPaperAbstract())
            .append("submitDate", getSubmitDate())
            .append("status", getStatus())
            .append("studentId", getStudentId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}