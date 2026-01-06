package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysDatabase;
import com.ruoyi.system.mapper.SysDatabaseMapper;
import com.ruoyi.system.service.ISysDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 数据库管理Service实现类
 * 
 * @author ruoyi
 */
@Service
public class SysDatabaseServiceImpl implements ISysDatabaseService
{
    @Autowired
    private SysDatabaseMapper sysDatabaseMapper;

    /**
     * 查询数据库管理列表
     */
    @Override
    public List<SysDatabase> selectSysDatabaseList(SysDatabase sysDatabase)
    {
        return sysDatabaseMapper.selectSysDatabaseList(sysDatabase);
    }

    /**
     * 获取数据库管理详细信息
     */
    @Override
    public SysDatabase selectSysDatabaseById(Long databaseId)
    {
        return sysDatabaseMapper.selectSysDatabaseById(databaseId);
    }

    /**
     * 新增数据库管理
     */
    @Override
    public int insertSysDatabase(SysDatabase sysDatabase)
    {
        sysDatabase.setCreateBy(SecurityUtils.getUsername());
        return sysDatabaseMapper.insertSysDatabase(sysDatabase);
    }

    /**
     * 修改数据库管理
     */
    @Override
    public int updateSysDatabase(SysDatabase sysDatabase)
    {
        sysDatabase.setUpdateBy(SecurityUtils.getUsername());
        return sysDatabaseMapper.updateSysDatabase(sysDatabase);
    }

    /**
     * 批量删除数据库管理
     */
    @Override
    public int deleteSysDatabaseByIds(Long[] databaseIds)
    {
        return sysDatabaseMapper.deleteSysDatabaseByIds(databaseIds);
    }

    /**
     * 删除数据库管理信息
     */
    @Override
    public int deleteSysDatabaseById(Long databaseId)
    {
        return sysDatabaseMapper.deleteSysDatabaseById(databaseId);
    }

    /**
     * 测试数据库连接
     */
    @Override
    public AjaxResult testConnection(SysDatabase sysDatabase)
    {
        Connection connection = null;
        try
        {
            Class.forName(sysDatabase.getDriverClass());
            connection = DriverManager.getConnection(
                sysDatabase.getUrl(),
                sysDatabase.getUsername(),
                sysDatabase.getPassword()
            );
            return AjaxResult.success("数据库连接成功");
        }
        catch (ClassNotFoundException e)
        {
            return AjaxResult.error("驱动类不存在: " + e.getMessage());
        }
        catch (SQLException e)
        {
            return AjaxResult.error("数据库连接失败: " + e.getMessage());
        }
        finally
        {
            if (connection != null)
            {
                try { connection.close(); }
                catch (SQLException e) { /* 忽略关闭异常 */ }
            }
        }
    }

    /**
     * 备份数据库
     */
    @Override
    public AjaxResult backupDatabase(SysDatabase sysDatabase)
    {
        String backupPath = sysDatabase.getBackupPath();
        if (StringUtils.isEmpty(backupPath))
        {
            backupPath = "D:/ruoyi/backup/database/";
        }

        File dir = new File(backupPath);
        if (!dir.exists())
        {
            dir.mkdirs();
        }

        String fileName = sysDatabase.getConnectionName() + "_" + 
            new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".sql";
        String filePath = backupPath + File.separator + fileName;

        // 这里简化处理，实际应根据数据库类型执行对应备份命令
        try
        {
            // 模拟备份操作
            File backupFile = new File(filePath);
            if (backupFile.createNewFile())
            {
                return AjaxResult.success("数据库备份成功", filePath);
            }
            else
            {
                return AjaxResult.error("备份文件创建失败");
            }
        }
        catch (IOException e)
        {
            return AjaxResult.error("备份失败: " + e.getMessage());
        }
    }

    /**
     * 恢复数据库
     */
    @Override
    public AjaxResult restoreDatabase(SysDatabase sysDatabase)
    {
        // 实际实现需根据数据库类型执行恢复命令
        if (StringUtils.isEmpty(sysDatabase.getBackupPath()))
        {
            return AjaxResult.error("备份文件路径不能为空");
        }

        File backupFile = new File(sysDatabase.getBackupPath());
        if (!backupFile.exists())
        {
            return AjaxResult.error("备份文件不存在");
        }

        // 模拟恢复操作
        return AjaxResult.success("数据库恢复成功");
    }

    /**
     * 导入数据
     */
    @Override
    public String importDatabase(MultipartFile file, boolean updateSupport, String operName)
    {
        ExcelUtil<SysDatabase> util = new ExcelUtil<SysDatabase>(SysDatabase.class);
        try
        {
            List<SysDatabase> databaseList = util.importExcel(file.getInputStream());
            return importDatabase(databaseList, updateSupport, operName);
        }
        catch (IOException e)
        {
            throw new ServiceException("导入数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 导入数据
     */
    @Override
    public String importDatabase(List<SysDatabase> databaseList, boolean updateSupport, String operName)
    {
        if (databaseList == null || databaseList.isEmpty())
        {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (SysDatabase database : databaseList)
        {
            try
            {
                SysDatabase existing = sysDatabaseMapper.selectSysDatabaseByName(database.getConnectionName());
                if (existing == null)
                {
                    database.setCreateBy(operName);
                    sysDatabaseMapper.insertSysDatabase(database);
                    successNum++;
                    successMsg.append("，").append(database.getConnectionName());
                }
                else if (updateSupport)
                {
                    database.setDatabaseId(existing.getDatabaseId());
                    database.setUpdateBy(operName);
                    sysDatabaseMapper.updateSysDatabase(database);
                    successNum++;
                    successMsg.append("，").append(database.getConnectionName());
                }
                else
                {
                    failureNum++;
                    failureMsg.append("，").append(database.getConnectionName()).append("(连接名称已存在)");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = database.getConnectionName() + "导入失败: " + e.getMessage();
                failureMsg.append("，").append(msg);
            }
        }

        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败 " + failureNum + " 条数据，原因如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    /**
     * 导出数据库管理列表
     */
    @Override
    public void exportSysDatabase(SysDatabase sysDatabase, HttpServletResponse response)
    {
        List<SysDatabase> list = sysDatabaseMapper.selectSysDatabaseList(sysDatabase);
        ExcelUtil<SysDatabase> util = new ExcelUtil<SysDatabase>(SysDatabase.class);
        util.exportExcel(response, list, "数据库管理数据");
    }

    /**
     * 查询数据库表结构信息
     */
    @Override
    public List<Map<String, Object>> selectDatabaseTables(Long databaseId)
    {
        SysDatabase db = sysDatabaseMapper.selectSysDatabaseById(databaseId);
        if (db == null)
        {
            throw new ServiceException("数据库连接配置不存在");
        }
        return sysDatabaseMapper.selectDatabaseTables(databaseId);
    }

    /**
     * 执行数据库备份
     */
    @Override
    public AjaxResult backupDatabase(Long databaseId)
    {
        SysDatabase db = sysDatabaseMapper.selectSysDatabaseById(databaseId);
        if (db == null)
        {
            return AjaxResult.error("数据库连接配置不存在");
        }

        String backupPath = db.getBackupPath();
        if (StringUtils.isEmpty(backupPath))
        {
            backupPath = "D:/ruoyi/backup/database/";
        }

        File dir = new File(backupPath);
        if (!dir.exists())
        {
            dir.mkdirs();
        }

        String fileName = db.getConnectionName() + "_" + 
            new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".sql";
        String filePath = backupPath + File.separator + fileName;

        // 实际备份逻辑应根据数据库类型执行对应命令
        // 此处仅演示调用Mapper更新备份时间
        int rows = sysDatabaseMapper.backupDatabase(databaseId);
        if (rows > 0)
        {
            return AjaxResult.success("数据库备份成功", filePath);
        }
        return AjaxResult.error("数据库备份失败");
    }

    /**
     * 执行数据库恢复
     */
    @Override
    public AjaxResult restoreDatabase(Long databaseId, String backupFile)
    {
        SysDatabase db = sysDatabaseMapper.selectSysDatabaseById(databaseId);
        if (db == null)
        {
            return AjaxResult.error("数据库连接配置不存在");
        }

        File file = new File(backupFile);
        if (!file.exists())
        {
            return AjaxResult.error("备份文件不存在");
        }

        // 实际恢复逻辑应根据数据库类型执行对应命令
        // 此处仅演示调用Mapper更新恢复时间
        Map<String, Object> params = new HashMap<>();
        params.put("databaseId", databaseId);
        params.put("backupFile", backupFile);
        int rows = sysDatabaseMapper.restoreDatabase(databaseId, backupFile);
        if (rows > 0)
        {
            return AjaxResult.success("数据库恢复成功");
        }
        return AjaxResult.error("数据库恢复失败");
    }
}