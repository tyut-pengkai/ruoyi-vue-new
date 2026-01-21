package com.ruoyi.web.controller.open3rd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Open3rdFileInfo
{
    private String id;
    private String name;
    private Long version;

    @JsonProperty("create_time")
    private Long createTime;

    @JsonProperty("update_time")
    private Long updateTime;

    @JsonProperty("creator_id")
    private String creatorId;

    @JsonProperty("modifier_id")
    private String modifierId;

    private Long size;

    @JsonProperty("disable_watermark")
    private Boolean disableWatermark;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Long getVersion()
    {
        return version;
    }

    public void setVersion(Long version)
    {
        this.version = version;
    }

    public Long getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Long createTime)
    {
        this.createTime = createTime;
    }

    public Long getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getCreatorId()
    {
        return creatorId;
    }

    public void setCreatorId(String creatorId)
    {
        this.creatorId = creatorId;
    }

    public String getModifierId()
    {
        return modifierId;
    }

    public void setModifierId(String modifierId)
    {
        this.modifierId = modifierId;
    }

    public Long getSize()
    {
        return size;
    }

    public void setSize(Long size)
    {
        this.size = size;
    }

    public Boolean getDisableWatermark()
    {
        return disableWatermark;
    }

    public void setDisableWatermark(Boolean disableWatermark)
    {
        this.disableWatermark = disableWatermark;
    }
}
