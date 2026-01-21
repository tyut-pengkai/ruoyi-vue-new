package com.ruoyi.web.controller.open3rd.service;

import java.io.File;

public class Open3rdFileDescriptor
{
    private final String fileId;
    private final String name;
    private final File file;
    private final String creatorId;
    private final String modifierId;
    private final Long version;
    private final Boolean disableWatermark;

    public Open3rdFileDescriptor(String fileId, String name, File file, String creatorId, String modifierId, Long version,
            Boolean disableWatermark)
    {
        this.fileId = fileId;
        this.name = name;
        this.file = file;
        this.creatorId = creatorId;
        this.modifierId = modifierId;
        this.version = version;
        this.disableWatermark = disableWatermark;
    }

    public String getFileId()
    {
        return fileId;
    }

    public String getName()
    {
        return name;
    }

    public File getFile()
    {
        return file;
    }

    public String getCreatorId()
    {
        return creatorId;
    }

    public String getModifierId()
    {
        return modifierId;
    }

    public Long getVersion()
    {
        return version;
    }

    public Boolean getDisableWatermark()
    {
        return disableWatermark;
    }
}
