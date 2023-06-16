package com.ruoyi.license.service;

import com.ruoyi.license.domain.SysLicenseRecord;

import java.util.List;

/**
 * 验证授权用户Service接口
 *
 * @author zwgu
 * @date 2022-09-08
 */
public interface ISysLicenseRecordService {
    /**
     * 查询验证授权用户
     *
     * @param id 验证授权用户主键
     * @return 验证授权用户
     */
    public SysLicenseRecord selectSysLicenseRecordById(Long id);

    public SysLicenseRecord selectSysLicenseRecordByLoginCode(String loginCode);

    public SysLicenseRecord selectSysLicenseRecordByDeviceCode(String deviceCode);

    /**
     * 查询验证授权用户列表
     *
     * @param sysLicenseRecord 验证授权用户
     * @return 验证授权用户集合
     */
    public List<SysLicenseRecord> selectSysLicenseRecordList(SysLicenseRecord sysLicenseRecord);

    /**
     * 新增验证授权用户
     *
     * @param sysLicenseRecord 验证授权用户
     * @return 结果
     */
    public int insertSysLicenseRecord(SysLicenseRecord sysLicenseRecord);

    /**
     * 修改验证授权用户
     *
     * @param sysLicenseRecord 验证授权用户
     * @return 结果
     */
    public int updateSysLicenseRecord(SysLicenseRecord sysLicenseRecord);

    /**
     * 批量删除验证授权用户
     *
     * @param ids 需要删除的验证授权用户主键集合
     * @return 结果
     */
    public int deleteSysLicenseRecordByIds(Long[] ids);

    /**
     * 删除验证授权用户信息
     *
     * @param id 验证授权用户主键
     * @return 结果
     */
    public int deleteSysLicenseRecordById(Long id);
}
