package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreQuickFunction;
import com.ruoyi.xkt.dto.storeQuickFunction.StoreQuickFuncDTO;
import com.ruoyi.xkt.mapper.StoreQuickFunctionMapper;
import com.ruoyi.xkt.service.IStoreQuickFunctionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 档口快捷功能Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreQuickFunctionServiceImpl implements IStoreQuickFunctionService {

    final StoreQuickFunctionMapper storeQuickFuncMapper;

    @Override
    @Transactional(readOnly = true)
    public List<StoreQuickFuncDTO.DetailDTO> getCheckedMenuList(Long storeId) {
        List<StoreQuickFunction> storeQuickFuncList = storeQuickFuncMapper.selectList(new LambdaQueryWrapper<StoreQuickFunction>()
                .eq(StoreQuickFunction::getStoreId,storeId).eq(StoreQuickFunction::getDelFlag,"0"));
        return CollectionUtils.isEmpty(storeQuickFuncList) ? new ArrayList<>() : BeanUtil.copyToList(storeQuickFuncList, StoreQuickFuncDTO.DetailDTO.class);
    }

    /**
     * 更新档口绑定的快捷功能
     *
     * @param storeQuickFuncDTO 绑定档口快捷功能的DTO
     * @return
     */
    @Override
    public void updateCheckedList(StoreQuickFuncDTO storeQuickFuncDTO) {
        // 先将旧的绑定关系置为无效
        this.storeQuickFuncMapper.updateDelFlagByStoreId(storeQuickFuncDTO.getStoreId());
        // 新增档口的快捷功能
        List<StoreQuickFunction> checkedList = BeanUtil.copyToList(storeQuickFuncDTO.getCheckedList(), StoreQuickFunction.class);
        checkedList.forEach(x -> x.setStoreId(storeQuickFuncDTO.getStoreId()));
        this.storeQuickFuncMapper.insert(checkedList);
    }

    /**
     * 查询档口快捷功能
     *
     * @param storeQuickFuncId 档口快捷功能主键
     * @return 档口快捷功能
     */
    @Override
    @Transactional(readOnly = true)
    public StoreQuickFunction selectStoreQuickFunctionByStoreQuickFuncId(Long storeQuickFuncId) {
        return storeQuickFuncMapper.selectStoreQuickFunctionByStoreQuickFuncId(storeQuickFuncId);
    }

    /**
     * 查询档口快捷功能列表
     *
     * @param storeQuickFunction 档口快捷功能
     * @return 档口快捷功能
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreQuickFunction> selectStoreQuickFunctionList(StoreQuickFunction storeQuickFunction) {
        return storeQuickFuncMapper.selectStoreQuickFunctionList(storeQuickFunction);
    }

    /**
     * 新增档口快捷功能
     *
     * @param storeQuickFunction 档口快捷功能
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreQuickFunction(StoreQuickFunction storeQuickFunction) {
        storeQuickFunction.setCreateTime(DateUtils.getNowDate());
        return storeQuickFuncMapper.insertStoreQuickFunction(storeQuickFunction);
    }

    /**
     * 修改档口快捷功能
     *
     * @param storeQuickFunction 档口快捷功能
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreQuickFunction(StoreQuickFunction storeQuickFunction) {
        storeQuickFunction.setUpdateTime(DateUtils.getNowDate());
        return storeQuickFuncMapper.updateStoreQuickFunction(storeQuickFunction);
    }

    /**
     * 批量删除档口快捷功能
     *
     * @param storeQuickFuncIds 需要删除的档口快捷功能主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreQuickFunctionByStoreQuickFuncIds(Long[] storeQuickFuncIds) {
        return storeQuickFuncMapper.deleteStoreQuickFunctionByStoreQuickFuncIds(storeQuickFuncIds);
    }

    /**
     * 删除档口快捷功能信息
     *
     * @param storeQuickFuncId 档口快捷功能主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteStoreQuickFunctionByStoreQuickFuncId(Long storeQuickFuncId) {
        return storeQuickFuncMapper.deleteStoreQuickFunctionByStoreQuickFuncId(storeQuickFuncId);
    }


}
