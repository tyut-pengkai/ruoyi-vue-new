package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysDatabase;
import java.util.List;

/**
 * 数据库管理Mapper接口
 * 
 * @author ruoyi
 */
public interface SysDatabaseMapper
{
    /**
     * 查询数据库管理列表
     * 
     * @param sysDatabase 数据库管理
     * @return 数据库管理集合
     */
    public List<SysDatabase> selectSysDatabaseList(SysDatabase sysDatabase);

    /**
     * 获取数据库管理详细信息
     * 
     * @param databaseId 数据库管理主键
     * @return 数据库管理
     */
    public SysDatabase selectSysDatabaseById(Long databaseId);

    /**
     * 根据连接名称查询数据库管理
     * 
     * @param connectionName 连接名称
     * @return 数据库管理
     */
    public SysDatabase selectSysDatabaseByName(String connectionName);

    /**
     * 新增数据库管理
     * 
     * @param sysDatabase 数据库管理
     * @return 结果
     */
    public int insertSysDatabase(SysDatabase sysDatabase);

    /**
     * 修改数据库管理
     * 
     * @param sysDatabase 数据库管理
     * @return 结果
     */
    public int updateSysDatabase(SysDatabase sysDatabase);

    /**
     * 删除数据库管理
     * 
     * @param databaseId 数据库管理主键
     * @return 结果
     */
    public int deleteSysDatabaseById(Long databaseId);

    /**
     * 批量删除数据库管理
     * 
     * @param databaseIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysDatabaseByIds(Long[] databaseIds);

    /**
     * 查询数据库表结构信息
     * 
     * @param databaseId 数据库连接ID
     * @return 表结构列表
     */
    public List<Map<String, Object>> selectDatabaseTables(Long databaseId);

    /**
     * 执行数据库备份
     * 
     * @param databaseId 数据库连接ID
     * @return 备份结果
     */
    public int backupDatabase(Long databaseId);

    /**
     * 执行数据库恢复
     * 
     * @param databaseId 数据库连接ID
     * @param backupFile 备份文件路径
     * @return 恢复结果
     */
    public int restoreDatabase(@Param("databaseId") Long databaseId, @Param("backupFile") String backupFile);
}