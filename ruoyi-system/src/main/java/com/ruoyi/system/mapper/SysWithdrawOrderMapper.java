package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysWithdrawOrder;

import java.util.List;

/**
 * 提现记录Mapper接口
 * 
 * @author zwgu
 * @date 2024-06-03
 */
public interface SysWithdrawOrderMapper 
{
    /**
     * 查询提现记录
     * 
     * @param id 提现记录主键
     * @return 提现记录
     */
    public SysWithdrawOrder selectSysWithdrawOrderById(Long id);

    /**
     * 查询提现记录列表
     * 
     * @param sysWithdrawOrder 提现记录
     * @return 提现记录集合
     */
    public List<SysWithdrawOrder> selectSysWithdrawOrderList(SysWithdrawOrder sysWithdrawOrder);

    /**
     * 新增提现记录
     * 
     * @param sysWithdrawOrder 提现记录
     * @return 结果
     */
    public int insertSysWithdrawOrder(SysWithdrawOrder sysWithdrawOrder);

    /**
     * 修改提现记录
     * 
     * @param sysWithdrawOrder 提现记录
     * @return 结果
     */
    public int updateSysWithdrawOrder(SysWithdrawOrder sysWithdrawOrder);

    /**
     * 删除提现记录
     * 
     * @param id 提现记录主键
     * @return 结果
     */
    public int deleteSysWithdrawOrderById(Long id);

    /**
     * 批量删除提现记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysWithdrawOrderByIds(Long[] ids);
}
