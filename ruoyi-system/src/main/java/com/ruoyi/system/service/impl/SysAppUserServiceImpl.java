package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysAppUserMapper;
import com.ruoyi.system.domain.SysAppUser;
import com.ruoyi.system.service.ISysAppUserService;

/**
 * 软件用户Service业务层处理
 * 
 * @author zwgu
 * @date 2021-11-09
 */
@Service
public class SysAppUserServiceImpl implements ISysAppUserService 
{
    @Autowired
    private SysAppUserMapper sysAppUserMapper;

    /**
     * 查询软件用户
     * 
     * @param appUserId 软件用户主键
     * @return 软件用户
     */
    @Override
    public SysAppUser selectSysAppUserByAppUserId(Long appUserId)
    {
        return sysAppUserMapper.selectSysAppUserByAppUserId(appUserId);
    }

    /**
     * 查询软件用户列表
     * 
     * @param sysAppUser 软件用户
     * @return 软件用户
     */
    @Override
    public List<SysAppUser> selectSysAppUserList(SysAppUser sysAppUser)
    {
        return sysAppUserMapper.selectSysAppUserList(sysAppUser);
    }

    /**
     * 新增软件用户
     * 
     * @param sysAppUser 软件用户
     * @return 结果
     */
    @Override
    public int insertSysAppUser(SysAppUser sysAppUser)
    {
        sysAppUser.setCreateTime(DateUtils.getNowDate());
        return sysAppUserMapper.insertSysAppUser(sysAppUser);
    }

    /**
     * 修改软件用户
     * 
     * @param sysAppUser 软件用户
     * @return 结果
     */
    @Override
    public int updateSysAppUser(SysAppUser sysAppUser)
    {
        sysAppUser.setUpdateTime(DateUtils.getNowDate());
        return sysAppUserMapper.updateSysAppUser(sysAppUser);
    }

    /**
     * 批量删除软件用户
     * 
     * @param appUserIds 需要删除的软件用户主键
     * @return 结果
     */
    @Override
    public int deleteSysAppUserByAppUserIds(Long[] appUserIds)
    {
        return sysAppUserMapper.deleteSysAppUserByAppUserIds(appUserIds);
    }

    /**
     * 删除软件用户信息
     * 
     * @param appUserId 软件用户主键
     * @return 结果
     */
    @Override
    public int deleteSysAppUserByAppUserId(Long appUserId)
    {
        return sysAppUserMapper.deleteSysAppUserByAppUserId(appUserId);
    }
}
