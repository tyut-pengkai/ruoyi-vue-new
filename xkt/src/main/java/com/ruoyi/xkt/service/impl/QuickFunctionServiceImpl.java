package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.QuickFunction;
import com.ruoyi.xkt.dto.quickFunction.QuickFuncResDTO;
import com.ruoyi.xkt.dto.quickFunction.QuickFuncUpdateDTO;
import com.ruoyi.xkt.mapper.QuickFunctionMapper;
import com.ruoyi.xkt.service.IQuickFunctionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public QuickFuncResDTO getCheckedMenuList() {
        Long userId = SecurityUtils.getUserIdSafe();
        if (ObjectUtils.isEmpty(userId)) {
            throw new ServiceException("用户未登录，请先登录!", HttpStatus.ERROR);
        }
        List<QuickFunction> quickFuncList = this.quickFuncMapper.selectList(new LambdaQueryWrapper<QuickFunction>()
                .eq(QuickFunction::getUserId, userId).eq(QuickFunction::getDelFlag, Constants.UNDELETED));
        return new QuickFuncResDTO().setMenuList(BeanUtil.copyToList(quickFuncList, QuickFuncResDTO.QFMenuDTO.class));
    }

    /**
     * 更新绑定的快捷功能
     *
     * @param updateDTO 绑定快捷功能的DTO
     * @return
     */
    @Override
    @Transactional
    public Integer update(QuickFuncUpdateDTO updateDTO) {
        // 先将旧的绑定关系置为无效
        List<QuickFunction> existList = this.quickFuncMapper.selectList(new LambdaQueryWrapper<QuickFunction>()
                .eq(QuickFunction::getUserId, updateDTO.getUserId()).eq(QuickFunction::getDelFlag, Constants.UNDELETED));
        if (ObjectUtils.isNotEmpty(existList)) {
            existList.forEach(x -> x.setDelFlag(Constants.DELETED));
            this.quickFuncMapper.updateById(existList);
        }
        // 再新增新的快捷功能绑定关系
        List<QuickFunction> checkedList = updateDTO.getMenuList().stream()
                .map(x -> BeanUtil.toBean(x, QuickFunction.class).setUserId(updateDTO.getUserId())).collect(Collectors.toList());
        return this.quickFuncMapper.insert(checkedList).size();
    }

}
