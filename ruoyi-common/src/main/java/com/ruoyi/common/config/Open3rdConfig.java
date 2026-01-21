package com.ruoyi.common.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文档中台接入配置
 */
@Component
@ConfigurationProperties(prefix = "open3rd")
public class Open3rdConfig
{
    /** 接入方 APP ID */
    private String appId;

    /** 接入方 APP Secret */
    private String appSecret;

    /** 文件根目录 */
    private String fileRoot;

    /** 文件列表配置 */
    private List<Open3rdFileConfig> files = new ArrayList<>();

    /** Token 映射配置 */
    private List<Open3rdTokenConfig> tokens = new ArrayList<>();

    /** 默认权限配置 */
    private Open3rdPermissionConfig permission = new Open3rdPermissionConfig();

    public String getAppId()
    {
        return appId;
    }

    public void setAppId(String appId)
    {
        this.appId = appId;
    }

    public String getAppSecret()
    {
        return appSecret;
    }

    public void setAppSecret(String appSecret)
    {
        this.appSecret = appSecret;
    }

    public String getFileRoot()
    {
        return fileRoot;
    }

    public void setFileRoot(String fileRoot)
    {
        this.fileRoot = fileRoot;
    }

    public List<Open3rdFileConfig> getFiles()
    {
        return files;
    }

    public void setFiles(List<Open3rdFileConfig> files)
    {
        this.files = files;
    }

    public List<Open3rdTokenConfig> getTokens()
    {
        return tokens;
    }

    public void setTokens(List<Open3rdTokenConfig> tokens)
    {
        this.tokens = tokens;
    }

    public Open3rdPermissionConfig getPermission()
    {
        return permission;
    }

    public void setPermission(Open3rdPermissionConfig permission)
    {
        this.permission = permission;
    }

    public static class Open3rdFileConfig
    {
        private String id;
        private String name;
        private String path;
        private Long version;
        private Boolean disableWatermark;
        private String creatorId;
        private String modifierId;

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

        public String getPath()
        {
            return path;
        }

        public void setPath(String path)
        {
            this.path = path;
        }

        public Long getVersion()
        {
            return version;
        }

        public void setVersion(Long version)
        {
            this.version = version;
        }

        public Boolean getDisableWatermark()
        {
            return disableWatermark;
        }

        public void setDisableWatermark(Boolean disableWatermark)
        {
            this.disableWatermark = disableWatermark;
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
    }

    public static class Open3rdTokenConfig
    {
        private String token;
        private String userId;

        public String getToken()
        {
            return token;
        }

        public void setToken(String token)
        {
            this.token = token;
        }

        public String getUserId()
        {
            return userId;
        }

        public void setUserId(String userId)
        {
            this.userId = userId;
        }
    }

    public static class Open3rdPermissionConfig
    {
        private boolean read = true;
        private boolean update;
        private boolean copy;
        private boolean comment;
        private boolean print;
        private boolean download = true;
        private boolean rename;
        private boolean history;
        private boolean manage;

        public boolean isRead()
        {
            return read;
        }

        public void setRead(boolean read)
        {
            this.read = read;
        }

        public boolean isUpdate()
        {
            return update;
        }

        public void setUpdate(boolean update)
        {
            this.update = update;
        }

        public boolean isCopy()
        {
            return copy;
        }

        public void setCopy(boolean copy)
        {
            this.copy = copy;
        }

        public boolean isComment()
        {
            return comment;
        }

        public void setComment(boolean comment)
        {
            this.comment = comment;
        }

        public boolean isPrint()
        {
            return print;
        }

        public void setPrint(boolean print)
        {
            this.print = print;
        }

        public boolean isDownload()
        {
            return download;
        }

        public void setDownload(boolean download)
        {
            this.download = download;
        }

        public boolean isRename()
        {
            return rename;
        }

        public void setRename(boolean rename)
        {
            this.rename = rename;
        }

        public boolean isHistory()
        {
            return history;
        }

        public void setHistory(boolean history)
        {
            this.history = history;
        }

        public boolean isManage()
        {
            return manage;
        }

        public void setManage(boolean manage)
        {
            this.manage = manage;
        }
    }
}
