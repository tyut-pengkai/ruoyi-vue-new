package com.easycode.cloud.domain;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.ibatis.type.Alias;

/**
 * 【请填写功能名称】对象 wms_kb_and_sw_task_log
 * 
 * @author ruoyi
 * @date 2024-11-19
 */
@Alias("WmsKbAndSwTaskLog")
public class WmsKbAndSwTaskLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;
    private Long  tenantId;

    /** 任务id(wms_task_inf的id) */
    @Excel(name = "任务id(wms_task_inf的id)")
    private String taskId;

    /** 任务状态 */

    private String taskStatus;

    /** sap物料凭证*/
    private String sapMaterialDocument;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getSapMaterialDocument() {
        return sapMaterialDocument;
    }

    public void setSapMaterialDocument(String sapMaterialDocument) {
        this.sapMaterialDocument = sapMaterialDocument;
    }

    @Override
    public Long getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("taskId", getTaskId())
                .append("tenantId", getTenantId())
            .append("taskStatus", getTaskStatus())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("sapMaterialDocument", getSapMaterialDocument())
            .toString();
    }
}
