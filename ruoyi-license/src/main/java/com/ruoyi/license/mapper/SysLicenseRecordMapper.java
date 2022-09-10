package com.ruoyi.license.mapper;

import com.ruoyi.license.domain.SysLicenseRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 验证授权用户Mapper接口
 *
 * @author zwgu
 * @date 2022-09-08
 */
@Repository
public interface SysLicenseRecordMapper {
    /**
     * 查询验证授权用户
     *
     * @param id 验证授权用户主键
     * @return 验证授权用户
     */
    public SysLicenseRecord selectSysLicenseRecordById(Long id);

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
     * 删除验证授权用户
     *
     * @param id 验证授权用户主键
     * @return 结果
     */
    public int deleteSysLicenseRecordById(Long id);

    /**
     * 批量删除验证授权用户
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysLicenseRecordByIds(Long[] ids);
}
