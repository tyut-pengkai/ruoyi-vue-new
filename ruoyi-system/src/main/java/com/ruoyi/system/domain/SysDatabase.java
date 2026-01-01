package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 数据库管理对象 sys_database
 * 
 * @author ruoyi
 */
public class SysDatabase extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long databaseId;

    /** 连接名称 */
    @Excel(name = "连接名称")
    private String connectionName;

    /** 数据库类型 */
    @Excel(name = "数据库类型")
    private String dbType;

    /** 驱动类名 */
    @Excel(name = "驱动类名")
    private String driverClass;

    /** 连接URL */
    @Excel(name = "连接URL")
    private String url;

    /** 数据库用户名 */
    @Excel(name = "数据库用户名")
    private String username;

    /** 数据库密码 */
    @Excel(name = "数据库密码")
    private String password;

    /** 备份路径 */
    @Excel(name = "备份路径")
    private String backupPath;

    /** 备份周期（天） */
    @Excel(name = "备份周期（天）")
    private Integer backupCycle;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setDatabaseId(Long databaseId)
    {
        this.databaseId = databaseId;
    }

    public Long getDatabaseId()
    {
        return databaseId;
    }
    public void setConnectionName(String connectionName)
    {
        this.connectionName = connectionName;
    }

    public String getConnectionName()
    {
        return connectionName;
    }
    public void setDbType(String dbType)
    {
        this.dbType = dbType;
    }

    public String getDbType()
    {
        return dbType;
    }
    public void setDriverClass(String driverClass)
    {
        this.driverClass = driverClass;
    }

    public String getDriverClass()
    {
        return driverClass;
    }
    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return url;
    }
    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUsername()
    {
        return username;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }
    public void setBackupPath(String backupPath)
    {
        this.backupPath = backupPath;
    }

    public String getBackupPath()
    {
        return backupPath;
    }
    public void setBackupCycle(Integer backupCycle)
    {
        this.backupCycle = backupCycle;
    }

    public Integer getBackupCycle()
    {
        return backupCycle;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("databaseId", getDatabaseId())
            .append("connectionName", getConnectionName())
            .append("dbType", getDbType())
            .append("driverClass", getDriverClass())
            .append("url", getUrl())
            .append("username", getUsername())
            .append("password", getPassword())
            .append("backupPath", getBackupPath())
            .append("backupCycle", getBackupCycle())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}