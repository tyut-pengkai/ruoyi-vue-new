package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysAppUser;

/**
 * 软件用户Service接口
 * 
 * @author zwgu
 * @date 2021-11-09
 */
public interface ISysAppUserService 
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
     * 批量删除软件用户
     * 
     * @param appUserIds 需要删除的软件用户主键集合
     * @return 结果
     */
    public int deleteSysAppUserByAppUserIds(Long[] appUserIds);

    /**
     * 删除软件用户信息
     * 
     * @param appUserId 软件用户主键
     * @return 结果
     */
    public int deleteSysAppUserByAppUserId(Long appUserId);
}
