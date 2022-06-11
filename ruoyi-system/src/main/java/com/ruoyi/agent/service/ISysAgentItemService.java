package com.ruoyi.agent.service;

import com.ruoyi.agent.domain.SysAgentItem;

import java.util.List;

/**
 * 代理卡类关联Service接口
 *
 * @author zwgu
 * @date 2022-06-11
 */
public interface ISysAgentItemService {
    /**
     * 查询代理卡类关联
     *
     * @param id 代理卡类关联主键
     * @return 代理卡类关联
     */
    public SysAgentItem selectSysAgentItemById(Long id);

    /**
     * 查询代理卡类关联列表
     *
     * @param sysAgentItem 代理卡类关联
     * @return 代理卡类关联集合
     */
    public List<SysAgentItem> selectSysAgentItemList(SysAgentItem sysAgentItem);

    /**
     * 新增代理卡类关联
     *
     * @param sysAgentItem 代理卡类关联
     * @return 结果
     */
    public int insertSysAgentItem(SysAgentItem sysAgentItem);

    /**
     * 修改代理卡类关联
     *
     * @param sysAgentItem 代理卡类关联
     * @return 结果
     */
    public int updateSysAgentItem(SysAgentItem sysAgentItem);

    /**
     * 批量删除代理卡类关联
     *
     * @param ids 需要删除的代理卡类关联主键集合
     * @return 结果
     */
    public int deleteSysAgentItemByIds(Long[] ids);

    /**
     * 删除代理卡类关联信息
     *
     * @param id 代理卡类关联主键
     * @return 结果
     */
    public int deleteSysAgentItemById(Long id);
}
