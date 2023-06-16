package com.ruoyi.license.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.license.domain.SysLicenseRecord;
import com.ruoyi.license.mapper.SysLicenseRecordMapper;
import com.ruoyi.license.service.ISysLicenseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 验证授权用户Service业务层处理
 *
 * @author zwgu
 * @date 2022-09-08
 */
@Service
public class SysLicenseRecordServiceImpl implements ISysLicenseRecordService {
    @Autowired
    private SysLicenseRecordMapper sysLicenseRecordMapper;

    /**
     * 查询验证授权用户
     *
     * @param id 验证授权用户主键
     * @return 验证授权用户
     */
    @Override
    public SysLicenseRecord selectSysLicenseRecordById(Long id) {
        return sysLicenseRecordMapper.selectSysLicenseRecordById(id);
    }

    @Override
    public SysLicenseRecord selectSysLicenseRecordByLoginCode(String loginCode) {
        return sysLicenseRecordMapper.selectSysLicenseRecordByLoginCode(loginCode);
    }

    @Override
    public SysLicenseRecord selectSysLicenseRecordByDeviceCode(String deviceCode) {
        return sysLicenseRecordMapper.selectSysLicenseRecordByDeviceCode(deviceCode);
    }

    /**
     * 查询验证授权用户列表
     *
     * @param sysLicenseRecord 验证授权用户
     * @return 验证授权用户
     */
    @Override
    public List<SysLicenseRecord> selectSysLicenseRecordList(SysLicenseRecord sysLicenseRecord) {
        return sysLicenseRecordMapper.selectSysLicenseRecordList(sysLicenseRecord);
    }

    /**
     * 新增验证授权用户
     *
     * @param sysLicenseRecord 验证授权用户
     * @return 结果
     */
    @Override
    public int insertSysLicenseRecord(SysLicenseRecord sysLicenseRecord) {
        sysLicenseRecord.setCreateTime(DateUtils.getNowDate());
        return sysLicenseRecordMapper.insertSysLicenseRecord(sysLicenseRecord);
    }

    /**
     * 修改验证授权用户
     *
     * @param sysLicenseRecord 验证授权用户
     * @return 结果
     */
    @Override
    public int updateSysLicenseRecord(SysLicenseRecord sysLicenseRecord) {
        sysLicenseRecord.setUpdateTime(DateUtils.getNowDate());
        return sysLicenseRecordMapper.updateSysLicenseRecord(sysLicenseRecord);
    }

    /**
     * 批量删除验证授权用户
     *
     * @param ids 需要删除的验证授权用户主键
     * @return 结果
     */
    @Override
    public int deleteSysLicenseRecordByIds(Long[] ids) {
        return sysLicenseRecordMapper.deleteSysLicenseRecordByIds(ids);
    }

    /**
     * 删除验证授权用户信息
     *
     * @param id 验证授权用户主键
     * @return 结果
     */
    @Override
    public int deleteSysLicenseRecordById(Long id) {
        return sysLicenseRecordMapper.deleteSysLicenseRecordById(id);
    }
}
