package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysDeviceCode;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.mapper.SysDeviceCodeMapper;
import com.ruoyi.system.service.ISysDeviceCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 机器码管理Service业务层处理
 * 
 * @author zwgu
 * @date 2021-12-06
 */
@Service
public class SysDeviceCodeServiceImpl implements ISysDeviceCodeService 
{
    @Autowired
    private SysDeviceCodeMapper sysDeviceCodeMapper;

    /**
     * 查询机器码管理
     * 
     * @param deviceCodeId 机器码管理主键
     * @return 机器码管理
     */
    @Override
    public SysDeviceCode selectSysDeviceCodeByDeviceCodeId(Long deviceCodeId)
    {
        return sysDeviceCodeMapper.selectSysDeviceCodeByDeviceCodeId(deviceCodeId);
    }

    /**
     * 查询机器码管理列表
     * 
     * @param sysDeviceCode 机器码管理
     * @return 机器码管理
     */
    @Override
    public List<SysDeviceCode> selectSysDeviceCodeList(SysDeviceCode sysDeviceCode)
    {
        return sysDeviceCodeMapper.selectSysDeviceCodeList(sysDeviceCode);
    }

    /**
     * 新增机器码管理
     * 
     * @param sysDeviceCode 机器码管理
     * @return 结果
     */
    @Override
    public int insertSysDeviceCode(SysDeviceCode sysDeviceCode)
    {
        sysDeviceCode.setCreateTime(DateUtils.getNowDate());
        return sysDeviceCodeMapper.insertSysDeviceCode(sysDeviceCode);
    }

    /**
     * 修改机器码管理
     * 
     * @param sysDeviceCode 机器码管理
     * @return 结果
     */
    @Override
    public int updateSysDeviceCode(SysDeviceCode sysDeviceCode)
    {
        sysDeviceCode.setUpdateTime(DateUtils.getNowDate());
        return sysDeviceCodeMapper.updateSysDeviceCode(sysDeviceCode);
    }

    /**
     * 批量删除机器码管理
     * 
     * @param deviceCodeIds 需要删除的机器码管理主键
     * @return 结果
     */
    @Override
    public int deleteSysDeviceCodeByDeviceCodeIds(Long[] deviceCodeIds)
    {
        return sysDeviceCodeMapper.deleteSysDeviceCodeByDeviceCodeIds(deviceCodeIds);
    }

    /**
     * 删除机器码管理信息
     * 
     * @param deviceCodeId 机器码管理主键
     * @return 结果
     */
    @Override
    public int deleteSysDeviceCodeByDeviceCodeId(Long deviceCodeId)
    {
        return sysDeviceCodeMapper.deleteSysDeviceCodeByDeviceCodeId(deviceCodeId);
    }

    /**
     * 修改状态
     *
     * @param deviceCode 信息
     * @return 结果
     */
    @Override
    public int updateSysDeviceCodeStatus(SysDeviceCode deviceCode) {
        return sysDeviceCodeMapper.updateSysDeviceCode(deviceCode);
    }

    /**
     * 查询机器码管理
     *
     * @param deviceCode 机器码
     * @return 机器码管理
     */
    @Override
    public SysDeviceCode selectSysDeviceCodeByDeviceCode(String deviceCode) {
        return sysDeviceCodeMapper.selectSysDeviceCodeByDeviceCode(deviceCode);
    }
}
