package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysAppTrialUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.mapper.SysAppTrialUserMapper;
import com.ruoyi.system.service.ISysAppTrialUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 试用信息Service业务层处理
 *
 * @author zwgu
 * @date 2022-08-01
 */
@Service
public class SysAppTrialUserServiceImpl extends ServiceImpl<SysAppTrialUserMapper, SysAppTrialUser> implements ISysAppTrialUserService {
    @Autowired
    private SysAppTrialUserMapper sysAppTrialUserMapper;

    /**
     * 查询试用信息
     *
     * @param appTrialUserId 试用信息主键
     * @return 试用信息
     */
    @Override
    public SysAppTrialUser selectSysAppTrialUserByAppTrialUserId(Long appTrialUserId) {
        return sysAppTrialUserMapper.selectSysAppTrialUserByAppTrialUserId(appTrialUserId);
    }

    @Override
    public List<SysAppTrialUser> selectSysAppTrialUserByLoginIp(String loginIp) {
        return sysAppTrialUserMapper.selectSysAppTrialUserByLoginIp(loginIp);
    }

    /**
     * 查询试用信息
     *
     * @param appId APP主键
     * @return 试用信息
     */
    @Override
    public SysAppTrialUser selectSysAppTrialUserByAppIdAndLoginIpAndDeviceCode(Long appId, String loginIp, String deviceCode) {
        return sysAppTrialUserMapper.selectSysAppTrialUserByAppIdAndLoginIpAndDeviceCode(appId, loginIp, deviceCode);
    }

    /**
     * 查询试用信息列表
     *
     * @param sysAppTrialUser 试用信息
     * @return 试用信息
     */
    @Override
    public List<SysAppTrialUser> selectSysAppTrialUserList(SysAppTrialUser sysAppTrialUser) {
        return sysAppTrialUserMapper.selectSysAppTrialUserList(sysAppTrialUser);
    }

    @Override
    public List<SysAppTrialUser> selectSysAppTrialUserListByAppIds(Long[] appIds) {
        return sysAppTrialUserMapper.selectSysAppTrialUserListByAppIds(appIds);
    }

    public List<SysAppTrialUser> selectSysAppTrialUserListByAppIdsAndNextEnableTimeBeforeNow(Long[] appIds) {
        return sysAppTrialUserMapper.selectSysAppTrialUserListByAppIdsAndNextEnableTimeBeforeNow(appIds);
    }

    /**
     * 新增试用信息
     *
     * @param sysAppTrialUser 试用信息
     * @return 结果
     */
    @Override
    public int insertSysAppTrialUser(SysAppTrialUser sysAppTrialUser) {
        sysAppTrialUser.setCreateTime(DateUtils.getNowDate());
        return sysAppTrialUserMapper.insertSysAppTrialUser(sysAppTrialUser);
    }

    /**
     * 修改试用信息
     *
     * @param sysAppTrialUser 试用信息
     * @return 结果
     */
    @Override
    public int updateSysAppTrialUser(SysAppTrialUser sysAppTrialUser) {
        sysAppTrialUser.setUpdateTime(DateUtils.getNowDate());
        return sysAppTrialUserMapper.updateSysAppTrialUser(sysAppTrialUser);
    }

    /**
     * 批量删除试用信息
     *
     * @param appTrialUserIds 需要删除的试用信息主键
     * @return 结果
     */
    @Override
    public int deleteSysAppTrialUserByAppTrialUserIds(Long[] appTrialUserIds) {
        return sysAppTrialUserMapper.deleteSysAppTrialUserByAppTrialUserIds(appTrialUserIds);
    }

    /**
     * 删除试用信息信息
     *
     * @param appTrialUserId 试用信息主键
     * @return 结果
     */
    @Override
    public int deleteSysAppTrialUserByAppTrialUserId(Long appTrialUserId) {
        return sysAppTrialUserMapper.deleteSysAppTrialUserByAppTrialUserId(appTrialUserId);
    }
}
