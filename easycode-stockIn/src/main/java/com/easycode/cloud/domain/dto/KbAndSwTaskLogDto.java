package com.easycode.cloud.domain.dto;

import com.weifu.cloud.common.core.annotation.Excel;
import com.weifu.cloud.common.core.web.domain.BaseEntity;
import org.apache.ibatis.type.Alias;

/**
 * 水位日志  wms_kb_and_sw_task_log
 */
@Alias("KbAndSwTaskLogDto")
public class KbAndSwTaskLogDto extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 处理人id
     */
    @Excel(name = "任务id")
    private Long taskId;

    /**
     * 任务号
     */
    @Excel(name = "任务状态")
    private String taskStatus;

    /**
     * 任务类型：字典提供
     */
    @Excel(name = "sap物料凭证")
    private String sapMaterialDocument;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
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
}
