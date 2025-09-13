package com.ruoyi.xkt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.xkt.domain.StoreSaleRefundRecord;
import com.ruoyi.xkt.dto.storeSaleRefundRecord.StoreSaleRefundRecordDTO;
import com.ruoyi.xkt.mapper.StoreSaleMapper;
import com.ruoyi.xkt.mapper.StoreSaleRefundRecordMapper;
import com.ruoyi.xkt.service.IStoreSaleRefundRecordService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 档口销售返单Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class StoreSaleRefundRecordServiceImpl implements IStoreSaleRefundRecordService {

    final StoreSaleRefundRecordMapper refundRecordMapper;
    final StoreSaleMapper storeSaleMapper;

    /**
     * 查询档口销售返单列表
     *
     * @param storeId     档口ID
     * @param storeSaleId 档口销售ID
     * @return 档口销售返单集合
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreSaleRefundRecordDTO> selectList(Long storeId, Long storeSaleId) {
        // 用户是否为档口管理者或子账户
        if (!SecurityUtils.isAdmin() && !SecurityUtils.isStoreManagerOrSub(storeId)) {
            throw new ServiceException("当前用户非档口管理者或子账号，无权限操作!", HttpStatus.ERROR);
        }
        List<StoreSaleRefundRecord> refundRecordList = this.refundRecordMapper.selectList(new LambdaQueryWrapper<StoreSaleRefundRecord>()
                .eq(StoreSaleRefundRecord::getStoreId, storeId).eq(StoreSaleRefundRecord::getStoreSaleId, storeSaleId).eq(StoreSaleRefundRecord::getDelFlag, Constants.UNDELETED));
        if (CollectionUtils.isEmpty(refundRecordList)) {
            return new ArrayList<>();
        }
        return refundRecordList.stream()
                .map(x -> BeanUtil.toBean(x, StoreSaleRefundRecordDTO.class).setStoreSaleRefundRecordId(x.getId()))
                .sorted(Comparator.comparing(StoreSaleRefundRecordDTO::getCreateTime).reversed())
                .collect(Collectors.toList());
    }

}
