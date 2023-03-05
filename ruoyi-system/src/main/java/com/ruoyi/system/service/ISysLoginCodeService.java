package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysLoginCode;

import java.util.List;

/**
 * 单码Service接口
 *
 * @author zwgu
 * @date 2021-12-03
 */
public interface ISysLoginCodeService {
    /**
     * 查询单码
     *
     * @param cardId 单码主键
     * @return 单码
     */
    public SysLoginCode selectSysLoginCodeByCardId(Long cardId);

    /**
     * 查询单码
     *
     * @param cardIds 单码主键
     * @return 单码
     */
    public List<SysLoginCode> selectSysLoginCodeByCardIds(Long[] cardIds);

    /**
     * 查询单码列表
     *
     * @param SysLoginCode 单码
     * @return 单码集合
     */
    public List<SysLoginCode> selectSysLoginCodeList(SysLoginCode SysLoginCode);

    public int countSysLoginCode(SysLoginCode SysLoginCode);

    /**
     * 新增单码
     *
     * @param SysLoginCode 单码
     * @return 结果
     */
    public int insertSysLoginCode(SysLoginCode SysLoginCode);

    /**
     * 修改单码
     *
     * @param SysLoginCode 单码
     * @return 结果
     */
    public int updateSysLoginCode(SysLoginCode SysLoginCode);

    /**
     * 批量删除单码
     *
     * @param cardIds 需要删除的单码主键集合
     * @return 结果
     */
    public int deleteSysLoginCodeByCardIds(Long[] cardIds);

    /**
     * 删除单码信息
     *
     * @param cardId 单码主键
     * @return 结果
     */
    public int deleteSysLoginCodeByCardId(Long cardId);

    /**
     * 查询单码
     *
     * @param cardNo
     * @return
     */
    public SysLoginCode selectSysLoginCodeByCardNo(String cardNo);

    /**
     * 查询单码
     */
    public SysLoginCode selectSysLoginCodeByAppIdAndCardNo(Long appId, String cardNo);

    /**
     * 新增单码
     *
     * @param SysLoginCodeList
     */
    public int insertSysLoginCodeBatch(List<SysLoginCode> SysLoginCodeList);

    /**
     * 导入卡密数据
     *
     * @param cardList        卡密数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    public String importLoginCode(List<SysLoginCode> cardList, Boolean isUpdateSupport, String operName);
}
