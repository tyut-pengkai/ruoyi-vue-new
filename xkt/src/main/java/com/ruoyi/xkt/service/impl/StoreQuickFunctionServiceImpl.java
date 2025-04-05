package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
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
                .eq(StoreQuickFunction::getStoreId, storeId).eq(StoreQuickFunction::getDelFlag, Constants.UNDELETED));
        return CollectionUtils.isEmpty(storeQuickFuncList) ? new ArrayList<>() : BeanUtil.copyToList(storeQuickFuncList, StoreQuickFuncDTO.DetailDTO.class);
    }

    /**
     * 更新档口绑定的快捷功能
     *
     * @param storeQuickFuncDTO 绑定档口快捷功能的DTO
     * @return
     */
    @Override
    @Transactional
    public void updateCheckedList(StoreQuickFuncDTO storeQuickFuncDTO) {
        // 先将旧的绑定关系置为无效
        this.storeQuickFuncMapper.updateDelFlagByStoreId(storeQuickFuncDTO.getStoreId());
        // 新增档口的快捷功能
        List<StoreQuickFunction> checkedList = BeanUtil.copyToList(storeQuickFuncDTO.getCheckedList(), StoreQuickFunction.class);
        checkedList.forEach(x -> x.setStoreId(storeQuickFuncDTO.getStoreId()));
        this.storeQuickFuncMapper.insert(checkedList);
    }

}
