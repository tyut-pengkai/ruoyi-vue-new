package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysCard;

import java.util.List;

/**
 * 卡密Service接口
 * 
 * @author zwgu
 * @date 2021-12-03
 */
public interface ISysCardService {
    /**
     * 查询卡密
     *
     * @param cardId 卡密主键
     * @return 卡密
     */
    public SysCard selectSysCardByCardId(Long cardId);

    /**
     * 查询卡密
     *
     * @param cardIds 卡密主键
     * @return 卡密
     */
    public List<SysCard> selectSysCardByCardIds(Long[] cardIds);

    /**
     * 查询卡密列表
     *
     * @param sysCard 卡密
     * @return 卡密集合
     */
    public List<SysCard> selectSysCardList(SysCard sysCard);

    public int countSysCard(SysCard sysCard);

    /**
     * 查询卡密列表
     *
     * @return 卡密集合
     */
    public List<String> selectSysCardNoList();

    /**
     * 新增卡密
     *
     * @param sysCard 卡密
     * @return 结果
     */
    public int insertSysCard(SysCard sysCard);

    /**
     * 修改卡密
     * 
     * @param sysCard 卡密
     * @return 结果
     */
    public int updateSysCard(SysCard sysCard);

    /**
     * 批量删除卡密
     * 
     * @param cardIds 需要删除的卡密主键集合
     * @return 结果
     */
    public int deleteSysCardByCardIds(Long[] cardIds);

    /**
     * 删除卡密信息
     * 
     * @param cardId 卡密主键
     * @return 结果
     */
    public int deleteSysCardByCardId(Long cardId);

    /**
     * 查询卡密
     *
     * @param cardNo
     * @return
     */
    public SysCard selectSysCardByCardNo(String cardNo);

    /**
     * 查询卡密
     */
    public SysCard selectSysCardByAppIdAndCardNo(Long appId, String cardNo);

    /**
     * 新增卡密
     *
     * @param sysCardList
     */
    public int insertSysCardBatch(List<SysCard> sysCardList);
}
