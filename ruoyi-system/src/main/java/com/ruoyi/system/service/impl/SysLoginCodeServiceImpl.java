package com.ruoyi.system.service.impl;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.mapper.SysLoginCodeMapper;
import com.ruoyi.system.service.ISysLoginCodeService;
import com.ruoyi.system.service.ISysLoginCodeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 单码Service业务层处理
 *
 * @author zwgu
 * @date 2021-12-03
 */
@Service
public class SysLoginCodeServiceImpl implements ISysLoginCodeService {
    @Autowired
    private SysLoginCodeMapper SysLoginCodeMapper;
    @Resource
    private ISysLoginCodeTemplateService SysLoginCodeTemplateService;

    /**
     * 查询单码
     *
     * @param cardId 单码主键
     * @return 单码
     */
    @Override
    public SysLoginCode selectSysLoginCodeByCardId(Long cardId) {
        return SysLoginCodeMapper.selectSysLoginCodeByCardId(cardId);
    }

    /**
     * 查询单码
     *
     * @param cardIds 单码主键
     * @return 单码
     */
    @Override
    public List<SysLoginCode> selectSysLoginCodeByCardIds(Long[] cardIds) {
        return SysLoginCodeMapper.selectSysLoginCodeByCardIds(cardIds);
    }

    /**
     * 查询单码列表
     *
     * @param SysLoginCode 单码
     * @return 单码
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysLoginCode> selectSysLoginCodeList(SysLoginCode SysLoginCode) {
        return SysLoginCodeMapper.selectSysLoginCodeList(SysLoginCode);
    }

    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public int countSysLoginCode(SysLoginCode SysLoginCode) {
        return SysLoginCodeMapper.countSysLoginCode(SysLoginCode);
    }

    /**
     * 新增单码
     *
     * @param SysLoginCode 单码
     * @return 结果
     */
    @Override
    public int insertSysLoginCode(SysLoginCode SysLoginCode) {
        SysLoginCode.setCreateTime(DateUtils.getNowDate());
        return SysLoginCodeMapper.insertSysLoginCode(SysLoginCode);
    }

    /**
     * 修改单码
     *
     * @param SysLoginCode 单码
     * @return 结果
     */
    @Override
    public int updateSysLoginCode(SysLoginCode SysLoginCode) {
        SysLoginCode.setUpdateTime(DateUtils.getNowDate());
        return SysLoginCodeMapper.updateSysLoginCode(SysLoginCode);
    }

    /**
     * 批量删除单码
     *
     * @param cardIds 需要删除的单码主键
     * @return 结果
     */
    @Override
    public int deleteSysLoginCodeByCardIds(Long[] cardIds) {
        return SysLoginCodeMapper.deleteSysLoginCodeByCardIds(cardIds);
    }

    /**
     * 删除单码信息
     *
     * @param cardId 单码主键
     * @return 结果
     */
    @Override
    public int deleteSysLoginCodeByCardId(Long cardId) {
        return SysLoginCodeMapper.deleteSysLoginCodeByCardId(cardId);
    }

    /**
     * 查询单码
     *
     * @param cardNo
     * @return
     */
    @Override
    public SysLoginCode selectSysLoginCodeByCardNo(String cardNo) {
        return SysLoginCodeMapper.selectSysLoginCodeByCardNo(cardNo);
    }

    /**
     * 查询单码
     */
    public SysLoginCode selectSysLoginCodeByAppIdAndCardNo(Long appId, String cardNo) {
        return SysLoginCodeMapper.selectSysLoginCodeByAppIdAndCardNo(appId, cardNo);
    }

    /**
     * 新增单码
     *
     * @param SysLoginCodeList
     */
    @Override
    public int insertSysLoginCodeBatch(List<SysLoginCode> SysLoginCodeList) {
        return SysLoginCodeMapper.insertSysLoginCodeBatch(SysLoginCodeList);
    }
}
