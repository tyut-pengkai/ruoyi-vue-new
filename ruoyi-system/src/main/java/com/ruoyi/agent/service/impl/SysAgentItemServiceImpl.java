package com.ruoyi.agent.service.impl;

import com.ruoyi.agent.domain.SysAgentItem;
import com.ruoyi.agent.domain.vo.TemplateInfoVo;
import com.ruoyi.agent.mapper.SysAgentItemMapper;
import com.ruoyi.agent.service.ISysAgentItemService;
import com.ruoyi.common.enums.TemplateType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 代理卡类关联Service业务层处理
 *
 * @author zwgu
 * @date 2022-06-11
 */
@Service
public class SysAgentItemServiceImpl implements ISysAgentItemService {
    @Autowired
    private SysAgentItemMapper sysAgentItemMapper;

    /**
     * 查询代理卡类关联
     *
     * @param id 代理卡类关联主键
     * @return 代理卡类关联
     */
    @Override
    public SysAgentItem selectSysAgentItemById(Long id) {
        return sysAgentItemMapper.selectSysAgentItemById(id);
    }

    /**
     * 查询代理卡类关联
     *
     * @param agentId 代理卡类关联主键
     * @return 代理卡类关联
     */
    @Override
    public List<SysAgentItem> selectSysAgentItemByAgentId(Long agentId) {
        return sysAgentItemMapper.selectSysAgentItemByAgentId(agentId);
    }

    /**
     * 查询代理卡类关联列表
     *
     * @param sysAgentItem 代理卡类关联
     * @return 代理卡类关联
     */
    @Override
    public List<SysAgentItem> selectSysAgentItemList(SysAgentItem sysAgentItem) {
        return sysAgentItemMapper.selectSysAgentItemList(sysAgentItem);
    }

    /**
     * 新增代理卡类关联
     *
     * @param sysAgentItem 代理卡类关联
     * @return 结果
     */
    @Override
    public int insertSysAgentItem(SysAgentItem sysAgentItem) {
        sysAgentItem.setCreateTime(DateUtils.getNowDate());
        return sysAgentItemMapper.insertSysAgentItem(sysAgentItem);
    }

    /**
     * 修改代理卡类关联
     *
     * @param sysAgentItem 代理卡类关联
     * @return 结果
     */
    @Override
    public int updateSysAgentItem(SysAgentItem sysAgentItem) {
        sysAgentItem.setUpdateTime(DateUtils.getNowDate());
        return sysAgentItemMapper.updateSysAgentItem(sysAgentItem);
    }

    /**
     * 批量删除代理卡类关联
     *
     * @param ids 需要删除的代理卡类关联主键
     * @return 结果
     */
    @Override
    public int deleteSysAgentItemByIds(Long[] ids) {
        return sysAgentItemMapper.deleteSysAgentItemByIds(ids);
    }

    /**
     * 删除代理卡类关联信息
     *
     * @param id 代理卡类关联主键
     * @return 结果
     */
    @Override
    public int deleteSysAgentItemById(Long id) {
        return sysAgentItemMapper.deleteSysAgentItemById(id);
    }

    /**
     * 检查代理指定卡类权限
     *
     * @param agentId
     * @param templateType
     * @param templateId
     * @return
     */
    public SysAgentItem checkAgentItem(TemplateInfoVo templateInfoVo, Long agentId, TemplateType templateType, Long templateId) {
        String prefix = templateInfoVo != null ? templateInfoVo.getReadableName() + " " : "";
        SysAgentItem s = new SysAgentItem();
        s.setAgentId(agentId);
        s.setTemplateType(templateType);
        s.setTemplateId(templateId);
        List<SysAgentItem> items = selectSysAgentItemList(s);
        if (items.isEmpty()) {
            throw new ServiceException(prefix + "您没有代理该卡的权限");
        } else if (items.size() == 1) {
            if (items.get(0).getExpireTime() != null && !items.get(0).getExpireTime().after(DateUtils.getNowDate())) {
                throw new ServiceException(prefix + "您代理该卡的权限已过期");
            }
            if(items.get(0).getAgentPrice() == null) {
                throw new ServiceException(prefix + "未配置代理价格");
            }
        } else {
            throw new ServiceException(prefix + "代理信息有误");
        }
        return items.get(0);
    }
}
