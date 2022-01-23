package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.system.mapper.SysAppUserMapper;
import com.ruoyi.system.service.ISysAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 查询软件用户
     *
     * @param appId 软件主键
     * @param userId 账号主键
     * @return 软件用户
     */
    @Override
    public SysAppUser selectSysAppUserByAppIdAndUserId(Long appId, Long userId) {
        return sysAppUserMapper.selectSysAppUserByAppIdAndUserId(appId, userId);
    }

    /**
     * 查询软件用户
     *
     * @param appId     软件主键
     * @param loginCode 单码
     * @return 软件用户
     */
    @Override
    public SysAppUser selectSysAppUserByAppIdAndLoginCode(Long appId, String loginCode) {
        return sysAppUserMapper.selectSysAppUserByAppIdAndLoginCode(appId, loginCode);
    }

    /**
     * 修改状态
     *
     * @param sysAppUser 信息
     * @return 结果
     */
    @Override
    public int updateSysDeviceCodeStatus(SysAppUser sysAppUser) {
        return sysAppUserMapper.updateSysAppUser(sysAppUser);
    }
}
