package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreQuickFunction;

import java.util.List;

/**
 * 档口快捷功能Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreQuickFunctionService {
    /**
     * 查询档口快捷功能
     *
     * @param storeQuickFuncId 档口快捷功能主键
     * @return 档口快捷功能
     */
    public StoreQuickFunction selectStoreQuickFunctionByStoreQuickFuncId(Long storeQuickFuncId);

    /**
     * 查询档口快捷功能列表
     *
     * @param storeQuickFunction 档口快捷功能
     * @return 档口快捷功能集合
     */
    public List<StoreQuickFunction> selectStoreQuickFunctionList(StoreQuickFunction storeQuickFunction);

    /**
     * 新增档口快捷功能
     *
     * @param storeQuickFunction 档口快捷功能
     * @return 结果
     */
    public int insertStoreQuickFunction(StoreQuickFunction storeQuickFunction);

    /**
     * 修改档口快捷功能
     *
     * @param storeQuickFunction 档口快捷功能
     * @return 结果
     */
    public int updateStoreQuickFunction(StoreQuickFunction storeQuickFunction);

    /**
     * 批量删除档口快捷功能
     *
     * @param storeQuickFuncIds 需要删除的档口快捷功能主键集合
     * @return 结果
     */
    public int deleteStoreQuickFunctionByStoreQuickFuncIds(Long[] storeQuickFuncIds);

    /**
     * 删除档口快捷功能信息
     *
     * @param storeQuickFuncId 档口快捷功能主键
     * @return 结果
     */
    public int deleteStoreQuickFunctionByStoreQuickFuncId(Long storeQuickFuncId);
}
