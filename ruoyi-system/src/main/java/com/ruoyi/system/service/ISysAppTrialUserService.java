package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysAppTrialUser;

import java.util.List;

/**
 * 试用信息Service接口
 *
 * @author zwgu
 * @date 2022-08-01
 */
public interface ISysAppTrialUserService {
    /**
     * 查询试用信息
     *
     * @param appTrialUserId 试用信息主键
     * @return 试用信息
     */
    public SysAppTrialUser selectSysAppTrialUserByAppTrialUserId(Long appTrialUserId);

    public List<SysAppTrialUser> selectSysAppTrialUserByLoginIp(String loginIp);

    /**
     * 查询试用信息
     *
     * @param appId APP主键
     * @return 试用信息
     */
    public SysAppTrialUser selectSysAppTrialUserByAppIdAndLoginIpAndDeviceCode(Long appId, String loginIp, String deviceCode);

    /**
     * 查询试用信息列表
     *
     * @param sysAppTrialUser 试用信息
     * @return 试用信息集合
     */
    public List<SysAppTrialUser> selectSysAppTrialUserList(SysAppTrialUser sysAppTrialUser);

    public List<SysAppTrialUser> selectSysAppTrialUserListByAppIds(Long[] appIds);

    /**
     * 新增试用信息
     *
     * @param sysAppTrialUser 试用信息
     * @return 结果
     */
    public int insertSysAppTrialUser(SysAppTrialUser sysAppTrialUser);

    /**
     * 修改试用信息
     *
     * @param sysAppTrialUser 试用信息
     * @return 结果
     */
    public int updateSysAppTrialUser(SysAppTrialUser sysAppTrialUser);

    /**
     * 批量删除试用信息
     *
     * @param appTrialUserIds 需要删除的试用信息主键集合
     * @return 结果
     */
    public int deleteSysAppTrialUserByAppTrialUserIds(Long[] appTrialUserIds);

    /**
     * 删除试用信息信息
     *
     * @param appTrialUserId 试用信息主键
     * @return 结果
     */
    public int deleteSysAppTrialUserByAppTrialUserId(Long appTrialUserId);
}
