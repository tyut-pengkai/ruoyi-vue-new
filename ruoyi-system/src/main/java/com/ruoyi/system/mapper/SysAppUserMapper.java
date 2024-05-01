package com.ruoyi.system.mapper;

import com.ruoyi.common.core.domain.entity.SysAppUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * 查询软件用户
     *
     * @param appId 软件主键
     * @param userId 账号主键
     * @return 软件用户
     */
    public SysAppUser selectSysAppUserByAppIdAndUserId(@Param("appId") Long appId, @Param("userId") Long userId);

    /**
     * 查询软件用户
     *
     * @param appId     软件主键
     * @param loginCode 单码
     * @return 软件用户
     */
    public SysAppUser selectSysAppUserByAppIdAndLoginCode(@Param("appId") Long appId, @Param("loginCode") String loginCode);

    public List<SysAppUser> selectSysAppUserListByAppIdsAndNextEnableUnbindTimeBeforeNow(Long[] appIds);
}
