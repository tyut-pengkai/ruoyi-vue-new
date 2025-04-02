package com.ruoyi.xkt.service.impl;

import com.ruoyi.xkt.domain.VoucherSequence;
import com.ruoyi.xkt.mapper.VoucherSequenceMapper;
import com.ruoyi.xkt.service.IVoucherSequenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 单据编号Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
@RequiredArgsConstructor
public class VoucherSequenceServiceImpl implements IVoucherSequenceService {

    final VoucherSequenceMapper sequenceMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public synchronized String generateCode(Long storeId, String type, String voucherDate) {
        VoucherSequence voucherSequence = this.sequenceMapper.queryByStoreIdAndType(storeId, type);
        String code = voucherSequence.getPrefix() + "-" + voucherDate + storeId + "-"
                + String.format(voucherSequence.getSequenceFormat(), voucherSequence.getNextSequence());
        sequenceMapper.updateById(voucherSequence.setNextSequence(voucherSequence.getNextSequence() + 1));
        return code;
    }

}
