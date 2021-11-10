package com.ruoyi.system.mapper;

import java.util.List;

import com.ruoyi.system.domain.SysAppUser;
import org.springframework.stereotype.Repository;

/**
 * 软件用户Mapper接口
 * 
 * @author zwgu
 * @date 2021-11-09
 */
@Repository
public interface SysAppUserMapper 
{
    /**
     * 查询软件用户
     * 
     * @param appUserId 软件用户主键
     * @return 软件用户
     */
    public SysAppUser selectSysAppUserByAppUserId(Long appUserId);

    /**
     * 查询软件用户列表
     * 
     * @param sysAppUser 软件用户
     * @return 软件用户集合
     */
    public List<SysAppUser> selectSysAppUserList(SysAppUser sysAppUser);

    /**
     * 新增软件用户
     * 
     * @param sysAppUser 软件用户
     * @return 结果
     */
    public int insertSysAppUser(SysAppUser sysAppUser);

    /**
     * 修改软件用户
     * 
     * @param sysAppUser 软件用户
     * @return 结果
     */
    public int updateSysAppUser(SysAppUser sysAppUser);

    /**
     * 删除软件用户
     * 
     * @param appUserId 软件用户主键
     * @return 结果
     */
    public int deleteSysAppUserByAppUserId(Long appUserId);

    /**
     * 批量删除软件用户
     * 
     * @param appUserIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAppUserByAppUserIds(Long[] appUserIds);
}
