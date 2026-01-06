package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.SysDatabase;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 数据库管理Service接口
 * 
 * @author ruoyi
 */
public interface ISysDatabaseService
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
     * 批量删除数据库管理
     * 
     * @param databaseIds 需要删除的数据库管理主键
     * @return 结果
     */
    public int deleteSysDatabaseByIds(Long[] databaseIds);

    /**
     * 删除数据库管理信息
     * 
     * @param databaseId 数据库管理主键
     * @return 结果
     */
    public int deleteSysDatabaseById(Long databaseId);

    /**
     * 测试数据库连接
     * 
     * @param sysDatabase 数据库管理
     * @return 结果
     */
    public AjaxResult testConnection(SysDatabase sysDatabase);

    /**
     * 备份数据库
     * 
     * @param sysDatabase 数据库管理
     * @return 结果
     */
    public AjaxResult backupDatabase(SysDatabase sysDatabase);

    /**
     * 恢复数据库
     * 
     * @param sysDatabase 数据库管理
     * @return 结果
     */
    public AjaxResult restoreDatabase(SysDatabase sysDatabase);

    /**
     * 导入数据
     * 
     * @param file 导入文件
     * @param updateSupport 是否更新支持
     * @param operName 操作用户
     * @return 结果
     */
    public String importDatabase(MultipartFile file, boolean updateSupport, String operName);
    
    /**
     * 导入数据
     * 
     * @param databaseList 数据库列表
     * @param updateSupport 是否更新支持
     * @param operName 操作用户
     * @return 结果
     */
    public String importDatabase(List<SysDatabase> databaseList, boolean updateSupport, String operName);

    /**
     * 导出数据库管理列表
     * 
     * @param sysDatabase 数据库管理
     * @param response 响应
     */
    public void exportSysDatabase(SysDatabase sysDatabase, HttpServletResponse response);
    
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
    public AjaxResult backupDatabase(Long databaseId);
    
    /**
     * 执行数据库恢复
     * 
     * @param databaseId 数据库连接ID
     * @param backupFile 备份文件路径
     * @return 恢复结果
     */
    public AjaxResult restoreDatabase(Long databaseId, String backupFile);
}