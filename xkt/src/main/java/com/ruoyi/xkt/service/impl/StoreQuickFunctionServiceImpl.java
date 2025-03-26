package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreQuickFunction;
import com.ruoyi.xkt.mapper.StoreQuickFunctionMapper;
import com.ruoyi.xkt.service.IStoreQuickFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 档口快捷功能Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreQuickFunctionServiceImpl implements IStoreQuickFunctionService {
    @Autowired
    private StoreQuickFunctionMapper storeQuickFunctionMapper;

    /**
     * 查询档口快捷功能
     *
     * @param storeQuickFuncId 档口快捷功能主键
     * @return 档口快捷功能
     */
    @Override
    public StoreQuickFunction selectStoreQuickFunctionByStoreQuickFuncId(Long storeQuickFuncId) {
        return storeQuickFunctionMapper.selectStoreQuickFunctionByStoreQuickFuncId(storeQuickFuncId);
    }

    /**
     * 查询档口快捷功能列表
     *
     * @param storeQuickFunction 档口快捷功能
     * @return 档口快捷功能
     */
    @Override
    public List<StoreQuickFunction> selectStoreQuickFunctionList(StoreQuickFunction storeQuickFunction) {
        return storeQuickFunctionMapper.selectStoreQuickFunctionList(storeQuickFunction);
    }

    /**
     * 新增档口快捷功能
     *
     * @param storeQuickFunction 档口快捷功能
     * @return 结果
     */
    @Override
    public int insertStoreQuickFunction(StoreQuickFunction storeQuickFunction) {
        storeQuickFunction.setCreateTime(DateUtils.getNowDate());
        return storeQuickFunctionMapper.insertStoreQuickFunction(storeQuickFunction);
    }

    /**
     * 修改档口快捷功能
     *
     * @param storeQuickFunction 档口快捷功能
     * @return 结果
     */
    @Override
    public int updateStoreQuickFunction(StoreQuickFunction storeQuickFunction) {
        storeQuickFunction.setUpdateTime(DateUtils.getNowDate());
        return storeQuickFunctionMapper.updateStoreQuickFunction(storeQuickFunction);
    }

    /**
     * 批量删除档口快捷功能
     *
     * @param storeQuickFuncIds 需要删除的档口快捷功能主键
     * @return 结果
     */
    @Override
    public int deleteStoreQuickFunctionByStoreQuickFuncIds(Long[] storeQuickFuncIds) {
        return storeQuickFunctionMapper.deleteStoreQuickFunctionByStoreQuickFuncIds(storeQuickFuncIds);
    }

    /**
     * 删除档口快捷功能信息
     *
     * @param storeQuickFuncId 档口快捷功能主键
     * @return 结果
     */
    @Override
    public int deleteStoreQuickFunctionByStoreQuickFuncId(Long storeQuickFuncId) {
        return storeQuickFunctionMapper.deleteStoreQuickFunctionByStoreQuickFuncId(storeQuickFuncId);
    }
}
