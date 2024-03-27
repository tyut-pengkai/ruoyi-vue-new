package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysCard;
import com.ruoyi.system.domain.vo.BatchNoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 卡密Mapper接口
 *
 * @author zwgu
 * @date 2021-12-03
 */
@Repository
public interface SysCardMapper {
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
     * 删除卡密
     *
     * @param cardId 卡密主键
     * @return 结果
     */
    public int deleteSysCardByCardId(Long cardId);

    /**
     * 批量删除卡密
     *
     * @param cardIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysCardByCardIds(Long[] cardIds);

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
    public SysCard selectSysCardByAppIdAndCardNo(@Param("appId") Long appId, @Param("cardNo") String cardNo);

    /**
     * 新增卡密
     *
     * @param sysCardList
     */
    public int insertSysCardBatch(List<SysCard> sysCardList);

    /**
     * 查询卡密列表
     *
     * @return 卡密集合
     */
    public List<String> selectSysCardNoList();

    /**
     * 获取批次号列表
     * @return
     */
    public List<BatchNoVo> selectBatchNoList(@Param("agentId")Long agentId);

    public List<Map<String, Long>> countSysCardAll(SysCard card);
}
