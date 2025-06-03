package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 文件对象 file_info
 * 
 * @author ruoyi
 * @date 2025-06-01
 */
public class FileInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 文件名称 */
    @Excel(name = "文件名称")
    private String fileName;

    /** 文件地址 */
    @Excel(name = "文件地址")
    private String fileUrl;

    /** 文件类型 */
    @Excel(name = "文件类型")
    private String fileType;

    /** 是否可编辑 0 否 1是 */
    @Excel(name = "是否可编辑 0 否 1是")
    private String isUpdate;

    /** 是否删除 0否 1是 */
    @Excel(name = "是否删除 0否 1是")
    private String isDelete;

    /** 是否共享 0 否 1是 */
    @Excel(name = "是否共享 0 否 1是")
    private String isShared;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }

    public void setFileUrl(String fileUrl) 
    {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() 
    {
        return fileUrl;
    }

    public void setFileType(String fileType) 
    {
        this.fileType = fileType;
    }

    public String getFileType() 
    {
        return fileType;
    }

    public void setIsUpdate(String isUpdate) 
    {
        this.isUpdate = isUpdate;
    }

    public String getIsUpdate() 
    {
        return isUpdate;
    }

    public void setIsDelete(String isDelete) 
    {
        this.isDelete = isDelete;
    }

    public String getIsDelete() 
    {
        return isDelete;
    }

    public void setIsShared(String isShared) 
    {
        this.isShared = isShared;
    }

    public String getIsShared() 
    {
        return isShared;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("fileName", getFileName())
            .append("fileUrl", getFileUrl())
            .append("fileType", getFileType())
            .append("isUpdate", getIsUpdate())
            .append("isDelete", getIsDelete())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("isShared", getIsShared())
            .toString();
    }
}
