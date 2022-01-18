package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysLoginCode;

import java.util.List;

/**
 * 登录码Service接口
 *
 * @author zwgu
 * @date 2021-12-03
 */
public interface ISysLoginCodeService {
    /**
     * 查询登录码
     *
     * @param cardId 登录码主键
     * @return 登录码
     */
    public SysLoginCode selectSysLoginCodeByCardId(Long cardId);

    /**
     * 查询登录码列表
     *
     * @param SysLoginCode 登录码
     * @return 登录码集合
     */
    public List<SysLoginCode> selectSysLoginCodeList(SysLoginCode SysLoginCode);

    /**
     * 新增登录码
     *
     * @param SysLoginCode 登录码
     * @return 结果
     */
    public int insertSysLoginCode(SysLoginCode SysLoginCode);

    /**
     * 修改登录码
     *
     * @param SysLoginCode 登录码
     * @return 结果
     */
    public int updateSysLoginCode(SysLoginCode SysLoginCode);

    /**
     * 批量删除登录码
     *
     * @param cardIds 需要删除的登录码主键集合
     * @return 结果
     */
    public int deleteSysLoginCodeByCardIds(Long[] cardIds);

    /**
     * 删除登录码信息
     *
     * @param cardId 登录码主键
     * @return 结果
     */
    public int deleteSysLoginCodeByCardId(Long cardId);

    /**
     * 查询登录码
     *
     * @param cardNo
     * @return
     */
    public SysLoginCode selectSysLoginCodeByCardNo(String cardNo);

    /**
     * 新增登录码
     *
     * @param SysLoginCodeList
     */
    public int insertSysLoginCodeBatch(List<SysLoginCode> SysLoginCodeList);
}
