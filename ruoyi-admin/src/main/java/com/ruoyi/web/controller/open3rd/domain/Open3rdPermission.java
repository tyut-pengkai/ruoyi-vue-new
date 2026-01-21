package com.ruoyi.web.controller.open3rd.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Open3rdPermission
{
    @JsonProperty("user_id")
    private String userId;
    private Boolean read;
    private Boolean update;
    private Boolean copy;
    private Boolean comment;
    private Boolean print;
    private Boolean download;
    private Boolean rename;
    private Boolean history;
    private Boolean manage;

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public Boolean getRead()
    {
        return read;
    }

    public void setRead(Boolean read)
    {
        this.read = read;
    }

    public Boolean getUpdate()
    {
        return update;
    }

    public void setUpdate(Boolean update)
    {
        this.update = update;
    }

    public Boolean getCopy()
    {
        return copy;
    }

    public void setCopy(Boolean copy)
    {
        this.copy = copy;
    }

    public Boolean getComment()
    {
        return comment;
    }

    public void setComment(Boolean comment)
    {
        this.comment = comment;
    }

    public Boolean getPrint()
    {
        return print;
    }

    public void setPrint(Boolean print)
    {
        this.print = print;
    }

    public Boolean getDownload()
    {
        return download;
    }

    public void setDownload(Boolean download)
    {
        this.download = download;
    }

    public Boolean getRename()
    {
        return rename;
    }

    public void setRename(Boolean rename)
    {
        this.rename = rename;
    }

    public Boolean getHistory()
    {
        return history;
    }

    public void setHistory(Boolean history)
    {
        this.history = history;
    }

    public Boolean getManage()
    {
        return manage;
    }

    public void setManage(Boolean manage)
    {
        this.manage = manage;
    }
}
