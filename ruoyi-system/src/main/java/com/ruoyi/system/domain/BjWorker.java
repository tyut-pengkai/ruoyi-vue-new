package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 技术人员管理对象 bj_worker
 * 
 * @author ssq
 * @date 2024-10-05
 */
public class BjWorker extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 技术人员姓名 */
    @Excel(name = "技术人员姓名")
    private String name;

    /** 工种 */
    @Excel(name = "工种")
    private String workType;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setWorkType(String workType) 
    {
        this.workType = workType;
    }

    public String getWorkType() 
    {
        return workType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("workType", getWorkType())
            .toString();
    }
}
