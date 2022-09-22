package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysCard;
import com.ruoyi.system.mapper.SysCardMapper;
import com.ruoyi.system.service.ISysCardService;
import com.ruoyi.system.service.ISysCardTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 卡密Service业务层处理
 * 
 * @author zwgu
 * @date 2021-12-03
 */
@Service
public class SysCardServiceImpl implements ISysCardService 
{
    @Autowired
    private SysCardMapper sysCardMapper;
    @Resource
    private ISysCardTemplateService sysCardTemplateService;

    /**
     * 查询卡密
     *
     * @param cardId 卡密主键
     * @return 卡密
     */
    @Override
    public SysCard selectSysCardByCardId(Long cardId) {
        return sysCardMapper.selectSysCardByCardId(cardId);
    }

    /**
     * 查询卡密
     *
     * @param cardIds 卡密主键
     * @return 卡密
     */
    @Override
    public List<SysCard> selectSysCardByCardIds(Long[] cardIds) {
        return sysCardMapper.selectSysCardByCardIds(cardIds);
    }

    /**
     * 查询卡密列表
     *
     * @param sysCard 卡密
     * @return 卡密
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysCard> selectSysCardList(SysCard sysCard) {
        return sysCardMapper.selectSysCardList(sysCard);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public int countSysCard(SysCard sysCard) {
        return sysCardMapper.countSysCard(sysCard);
    }

    /**
     * 查询卡密列表
     *
     * @return 卡密
     */
    @Override
    public List<String> selectSysCardNoList() {
        return sysCardMapper.selectSysCardNoList();
    }

    /**
     * 新增卡密
     *
     * @param sysCard 卡密
     * @return 结果
     */
    @Override
    public int insertSysCard(SysCard sysCard) {
        sysCard.setCreateTime(DateUtils.getNowDate());
        return sysCardMapper.insertSysCard(sysCard);
    }

    /**
     * 修改卡密
     * 
     * @param sysCard 卡密
     * @return 结果
     */
    @Override
    public int updateSysCard(SysCard sysCard)
    {
        sysCard.setUpdateTime(DateUtils.getNowDate());
        return sysCardMapper.updateSysCard(sysCard);
    }

    /**
     * 批量删除卡密
     * 
     * @param cardIds 需要删除的卡密主键
     * @return 结果
     */
    @Override
    public int deleteSysCardByCardIds(Long[] cardIds)
    {
        return sysCardMapper.deleteSysCardByCardIds(cardIds);
    }

    /**
     * 删除卡密信息
     * 
     * @param cardId 卡密主键
     * @return 结果
     */
    @Override
    public int deleteSysCardByCardId(Long cardId)
    {
        return sysCardMapper.deleteSysCardByCardId(cardId);
    }

    /**
     * 查询卡密
     *
     * @param cardNo
     * @return
     */
    @Override
    public SysCard selectSysCardByCardNo(String cardNo) {
        return sysCardMapper.selectSysCardByCardNo(cardNo);
    }

    /**
     * 查询卡密
     */
    public SysCard selectSysCardByAppIdAndCardNo(Long appId, String cardNo) {
        return sysCardMapper.selectSysCardByAppIdAndCardNo(appId, cardNo);
    }

    /**
     * 新增卡密
     *
     * @param sysCardList
     */
    @Override
    public int insertSysCardBatch(List<SysCard> sysCardList) {
        return sysCardMapper.insertSysCardBatch(sysCardList);
    }
}
