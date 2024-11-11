package com.ruoyi.system.domain;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物料对象 t_material
 * 
 * @author ruoyi
 * @date 2024-10-31
 */
public class TMaterial extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String materialCode;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 父编码 */
    @Excel(name = "父编码")
    private Long parentId;
    
    private TMaterial parentMaterial;
    
    private List<TMaterial> child;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setMaterialCode(String materialCode) 
    {
        this.materialCode = materialCode;
    }

    public String getMaterialCode() 
    {
        return materialCode;
    }
    public void setMaterialName(String materialName) 
    {
        this.materialName = materialName;
    }

    public String getMaterialName() 
    {
        return materialName;
    }
    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }

    public TMaterial getParentMaterial() {
		return parentMaterial;
	}

	public void setParentMaterial(TMaterial parentMaterial) {
		this.parentMaterial = parentMaterial;
	}

	public List<TMaterial> getChild() {
		return child;
	}

	public void setChild(List<TMaterial> child) {
		this.child = child;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("materialCode", getMaterialCode())
            .append("materialName", getMaterialName())
            .append("parentId", getParentId())
            .append("createTime", getCreateTime())
            .toString();
    }
}
