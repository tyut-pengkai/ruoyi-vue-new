package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysWithdrawMethod;

import java.util.List;

/**
 * 收款方式Mapper接口
 * 
 * @author zwgu
 * @date 2024-06-03
 */
public interface SysWithdrawMethodMapper 
{
    /**
     * 查询收款方式
     * 
     * @param id 收款方式主键
     * @return 收款方式
     */
    public SysWithdrawMethod selectSysWithdrawMethodById(Long id);

    /**
     * 查询收款方式列表
     * 
     * @param sysWithdrawMethod 收款方式
     * @return 收款方式集合
     */
    public List<SysWithdrawMethod> selectSysWithdrawMethodList(SysWithdrawMethod sysWithdrawMethod);

    /**
     * 新增收款方式
     * 
     * @param sysWithdrawMethod 收款方式
     * @return 结果
     */
    public int insertSysWithdrawMethod(SysWithdrawMethod sysWithdrawMethod);

    /**
     * 修改收款方式
     * 
     * @param sysWithdrawMethod 收款方式
     * @return 结果
     */
    public int updateSysWithdrawMethod(SysWithdrawMethod sysWithdrawMethod);

    /**
     * 删除收款方式
     * 
     * @param id 收款方式主键
     * @return 结果
     */
    public int deleteSysWithdrawMethodById(Long id);

    /**
     * 批量删除收款方式
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysWithdrawMethodByIds(Long[] ids);
}
