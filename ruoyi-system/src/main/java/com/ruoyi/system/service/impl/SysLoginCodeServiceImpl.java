package com.ruoyi.system.service.impl;

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
 * 登录码Service业务层处理
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
     * 查询登录码
     *
     * @param cardId 登录码主键
     * @return 登录码
     */
    @Override
    public SysLoginCode selectSysLoginCodeByCardId(Long cardId) {
        return SysLoginCodeMapper.selectSysLoginCodeByCardId(cardId);
    }

    /**
     * 查询登录码列表
     *
     * @param SysLoginCode 登录码
     * @return 登录码
     */
    @Override
    public List<SysLoginCode> selectSysLoginCodeList(SysLoginCode SysLoginCode) {
        return SysLoginCodeMapper.selectSysLoginCodeList(SysLoginCode);
    }

    /**
     * 新增登录码
     *
     * @param SysLoginCode 登录码
     * @return 结果
     */
    @Override
    public int insertSysLoginCode(SysLoginCode SysLoginCode) {
        SysLoginCode.setCreateTime(DateUtils.getNowDate());
        return SysLoginCodeMapper.insertSysLoginCode(SysLoginCode);
    }

    /**
     * 修改登录码
     *
     * @param SysLoginCode 登录码
     * @return 结果
     */
    @Override
    public int updateSysLoginCode(SysLoginCode SysLoginCode) {
        SysLoginCode.setUpdateTime(DateUtils.getNowDate());
        return SysLoginCodeMapper.updateSysLoginCode(SysLoginCode);
    }

    /**
     * 批量删除登录码
     *
     * @param cardIds 需要删除的登录码主键
     * @return 结果
     */
    @Override
    public int deleteSysLoginCodeByCardIds(Long[] cardIds) {
        return SysLoginCodeMapper.deleteSysLoginCodeByCardIds(cardIds);
    }

    /**
     * 删除登录码信息
     *
     * @param cardId 登录码主键
     * @return 结果
     */
    @Override
    public int deleteSysLoginCodeByCardId(Long cardId) {
        return SysLoginCodeMapper.deleteSysLoginCodeByCardId(cardId);
    }

    /**
     * 查询登录码
     *
     * @param cardNo
     * @return
     */
    @Override
    public SysLoginCode selectSysLoginCodeByCardNo(String cardNo) {
        return SysLoginCodeMapper.selectSysLoginCodeByCardNo(cardNo);
    }

    /**
     * 新增登录码
     *
     * @param SysLoginCodeList
     */
    @Override
    public int insertSysLoginCodeBatch(List<SysLoginCode> SysLoginCodeList) {
        return SysLoginCodeMapper.insertSysLoginCodeBatch(SysLoginCodeList);
    }
}
