package com.easycode.cloud.domain;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

/**
 * 送检单附件对象 wms_inspect_attachs
 *
 * @author fangshucheng
 * @date 2023-03-31
 */
@Alias("InspectAttachs")
public class InspectAttachs extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 送检单id */
    @Excel(name = "送检单id")
    private Long inspectOrderId;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String name;

    /** 文件地址 */
    @Excel(name = "文件地址")
    private String url;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 租户号 */
    @Excel(name = "租户号")
    private Long tenantId;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setInspectOrderId(Long inspectOrderId)
    {
        this.inspectOrderId = inspectOrderId;
    }

    public Long getInspectOrderId()
    {
        return inspectOrderId;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public Long getTenantId()
    {
        return tenantId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("inspectOrderId", getInspectOrderId())
                .append("name", getName())
                .append("url", getUrl())
                .append("status", getStatus())
                .append("tenantId", getTenantId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
