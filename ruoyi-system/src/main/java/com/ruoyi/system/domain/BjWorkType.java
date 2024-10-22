package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 工种管理对象 bj_work_type
 * 
 * @author ssq
 * @date 2024-10-05
 */
public class BjWorkType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 工种 */
    @Excel(name = "工种")
    private String workType;
    private Integer types;
    private Integer sorts;
    private Integer priceType;
    private String calFormula;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setWorkType(String workType) 
    {
        this.workType = workType;
    }

    public String getWorkType() 
    {
        return workType;
    }

    public Integer getTypes() {
		return types;
	}

	public void setTypes(Integer types) {
		this.types = types;
	}

	public Integer getSorts() {
		return sorts;
	}

	public void setSorts(Integer sorts) {
		this.sorts = sorts;
	}

	public Integer getPriceType() {
		return priceType;
	}

	public void setPriceType(Integer priceType) {
		this.priceType = priceType;
	}

	public String getCalFormula() {
		return calFormula;
	}

	public void setCalFormula(String calFormula) {
		this.calFormula = calFormula;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("workType", getWorkType())
            .toString();
    }
}
