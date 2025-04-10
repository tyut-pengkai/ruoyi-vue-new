package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.xkt.domain.QuickFunction;
import com.ruoyi.xkt.dto.quickFunction.QuickFuncDTO;
import com.ruoyi.xkt.mapper.QuickFunctionMapper;
import com.ruoyi.xkt.service.IQuickFunctionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 快捷功能Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class QuickFunctionServiceImpl implements IQuickFunctionService {

    final QuickFunctionMapper quickFuncMapper;

    @Override
    @Transactional(readOnly = true)
    public List<QuickFuncDTO.DetailDTO> getCheckedMenuList(Long roleId, Long bizId) {
        List<QuickFunction> storeQuickFuncList = quickFuncMapper.selectList(new LambdaQueryWrapper<QuickFunction>()
                .eq(QuickFunction::getBizId, bizId).eq(QuickFunction::getRoleId, roleId).eq(QuickFunction::getDelFlag, Constants.UNDELETED));
        return CollectionUtils.isEmpty(storeQuickFuncList) ? new ArrayList<>() : BeanUtil.copyToList(storeQuickFuncList, QuickFuncDTO.DetailDTO.class);
    }

    /**
     * 更新绑定的快捷功能
     *
     * @param storeQuickFuncDTO 绑定快捷功能的DTO
     * @return
     */
    @Override
    @Transactional
    public void updateCheckedList(QuickFuncDTO storeQuickFuncDTO) {
        // 先将旧的绑定关系置为无效
        this.quickFuncMapper.updateDelFlagByRoleIdAndBizId(storeQuickFuncDTO.getRoleId(), storeQuickFuncDTO.getBizId());
        // 新增档口的快捷功能
        List<QuickFunction> checkedList = BeanUtil.copyToList(storeQuickFuncDTO.getCheckedList(), QuickFunction.class);
        checkedList.forEach(x -> x.setBizId(storeQuickFuncDTO.getBizId()).setRoleId(storeQuickFuncDTO.getRoleId()));
        this.quickFuncMapper.insert(checkedList);
    }

}
