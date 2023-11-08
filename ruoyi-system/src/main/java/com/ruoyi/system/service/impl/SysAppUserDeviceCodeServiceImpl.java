package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysAppUserDeviceCode;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.mapper.SysAppUserDeviceCodeMapper;
import com.ruoyi.system.service.ISysAppUserDeviceCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 软件用户与设备码关联Service业务层处理
 *
 * @author zwgu
 * @date 2021-12-18
 */
@Service
public class SysAppUserDeviceCodeServiceImpl implements ISysAppUserDeviceCodeService {
    @Autowired
    private SysAppUserDeviceCodeMapper sysAppUserDeviceCodeMapper;

    /**
     * 查询软件用户与设备码关联
     *
     * @param id 软件用户与设备码关联主键
     * @return 软件用户与设备码关联
     */
    @Override
    public SysAppUserDeviceCode selectSysAppUserDeviceCodeById(Long id) {
        return sysAppUserDeviceCodeMapper.selectSysAppUserDeviceCodeById(id);
    }

    /**
     * 查询软件用户与设备码关联列表
     *
     * @param sysAppUserDeviceCode 软件用户与设备码关联
     * @return 软件用户与设备码关联
     */
    @Override
    public List<SysAppUserDeviceCode> selectSysAppUserDeviceCodeList(SysAppUserDeviceCode sysAppUserDeviceCode) {
        return sysAppUserDeviceCodeMapper.selectSysAppUserDeviceCodeList(sysAppUserDeviceCode);
    }

    /**
     * 新增软件用户与设备码关联
     *
     * @param sysAppUserDeviceCode 软件用户与设备码关联
     * @return 结果
     */
    @Override
    public int insertSysAppUserDeviceCode(SysAppUserDeviceCode sysAppUserDeviceCode) {
        sysAppUserDeviceCode.setCreateTime(DateUtils.getNowDate());
        sysAppUserDeviceCode.setCreateBy(SecurityUtils.getUsernameNoException());
        return sysAppUserDeviceCodeMapper.insertSysAppUserDeviceCode(sysAppUserDeviceCode);
    }

    /**
     * 修改软件用户与设备码关联
     *
     * @param sysAppUserDeviceCode 软件用户与设备码关联
     * @return 结果
     */
    @Override
    public int updateSysAppUserDeviceCode(SysAppUserDeviceCode sysAppUserDeviceCode) {
        sysAppUserDeviceCode.setUpdateTime(DateUtils.getNowDate());
        sysAppUserDeviceCode.setUpdateBy(SecurityUtils.getUsernameNoException());
        return sysAppUserDeviceCodeMapper.updateSysAppUserDeviceCode(sysAppUserDeviceCode);
    }

    /**
     * 批量删除软件用户与设备码关联
     *
     * @param ids 需要删除的软件用户与设备码关联主键
     * @return 结果
     */
    @Override
    public int deleteSysAppUserDeviceCodeByIds(Long[] ids) {
        return sysAppUserDeviceCodeMapper.deleteSysAppUserDeviceCodeByIds(ids);
    }

    /**
     * 删除软件用户与设备码关联信息
     *
     * @param id 软件用户与设备码关联主键
     * @return 结果
     */
    @Override
    public int deleteSysAppUserDeviceCodeById(Long id) {
        return sysAppUserDeviceCodeMapper.deleteSysAppUserDeviceCodeById(id);
    }

    /**
     * 修改状态
     *
     * @param appUserDeviceCode 信息
     * @return 结果
     */
    @Override
    public int updateSysDeviceCodeStatus(SysAppUserDeviceCode appUserDeviceCode) {
        return sysAppUserDeviceCodeMapper.updateSysAppUserDeviceCode(appUserDeviceCode);
    }

    /**
     * 查询软件用户与设备码关联
     *
     * @param appUserId
     * @param deviceCodeId
     * @return 软件用户与设备码关联
     */
    @Override
    public SysAppUserDeviceCode selectSysAppUserDeviceCodeByAppUserIdAndDeviceCodeId(Long appUserId, Long deviceCodeId) {
        return sysAppUserDeviceCodeMapper.selectSysAppUserDeviceCodeByAppUserIdAndDeviceCodeId(appUserId, deviceCodeId);
    }

    /**
     * 查询软件用户与设备码关联
     *
     * @param appUserId
     * @return 软件用户与设备码关联
     */
    @Override
    public List<SysAppUserDeviceCode> selectSysAppUserDeviceCodeByAppUserId(Long appUserId) {
        return sysAppUserDeviceCodeMapper.selectSysAppUserDeviceCodeByAppUserId(appUserId);
    }

    /**
     * 查询软件用户与设备码关联
     *
     * @param deviceCodeId
     * @return 软件用户与设备码关联
     */
    @Override
    public List<SysAppUserDeviceCode> selectSysAppUserDeviceCodeByAppIdAndDeviceCodeId(Long appId, Long deviceCodeId) {
        return sysAppUserDeviceCodeMapper.selectSysAppUserDeviceCodeByAppIdAndDeviceCodeId(appId, deviceCodeId);
    }
}
