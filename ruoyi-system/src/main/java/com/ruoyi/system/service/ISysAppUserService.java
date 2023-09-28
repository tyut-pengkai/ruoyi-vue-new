package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.model.LoginUser;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    /**
     * 查询软件用户
     *
     * @param appId 软件主键
     * @param userId 账号主键
     * @return 软件用户
     */
    public SysAppUser selectSysAppUserByAppIdAndUserId(Long appId, Long userId);

    /**
     * 查询软件用户
     *
     * @param appId     软件主键
     * @param loginCode 单码
     * @return 软件用户
     */
    SysAppUser selectSysAppUserByAppIdAndLoginCode(Long appId, String loginCode);

    /**
     * 修改状态
     *
     * @param sysAppUser 信息
     * @return 结果
     */
    public int updateSysDeviceCodeStatus(SysAppUser sysAppUser);

    /**
     * 计算当前实时在线数量
     * @param appUserId 不为空时取特定appUserId的统计数据
     * @return 返回两个key, u->Map&lt;Long, Set&lt;LoginUser&gt;，m->Map&lt;Long, Set&lt;Long&gt;，其中Set&lt;Long&gt;为设备码ID
     */
    public Map<String, Object> computeCurrentOnline(Long appUserId);

    /**
     * 计算当前实时在线数量
     * @return 返回两个key, u->Map&lt;Long, Set&lt;LoginUser&gt;，m->Map&lt;Long, Set&lt;Long&gt;，其中Set&lt;Long&gt;为设备码ID
     */
    public Map<String, Object> computeCurrentOnline();

    /**
     * 填充生效用户数、设备数限制和实时在线数量
     * @param sau
     */
    public void fillCurrentOnlineInfo(SysAppUser sau, Map<Long, Set<LoginUser>> onlineListUMap, Map<Long, Set<Long>> onlineListMMap);

    public void fillCurrentOnlineInfo(SysAppUser sau);
}
