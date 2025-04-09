package com.ruoyi.xkt.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.xkt.domain.ExternalAccount;
import com.ruoyi.xkt.domain.ExternalAccountTransDetail;
import com.ruoyi.xkt.dto.finance.TransInfo;
import com.ruoyi.xkt.enums.EAccountStatus;
import com.ruoyi.xkt.enums.EEntryStatus;
import com.ruoyi.xkt.enums.ELoanDirection;
import com.ruoyi.xkt.mapper.ExternalAccountMapper;
import com.ruoyi.xkt.mapper.ExternalAccountTransDetailMapper;
import com.ruoyi.xkt.service.IExternalAccountService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author liangyq
 * @date 2025-04-08 21:14
 */
@Service
public class ExternalAccountServiceImpl implements IExternalAccountService {

    @Autowired
    private ExternalAccountMapper externalAccountMapper;
    @Autowired
    private ExternalAccountTransDetailMapper externalAccountTransDetailMapper;

    @Override
    public ExternalAccount getById(Long id) {
        Assert.notNull(id);
        return externalAccountMapper.selectById(id);
    }

    @Transactional
    @Override
    public int addTransDetail(Long externalAccountId, TransInfo transInfo, ELoanDirection loanDirection,
                              EEntryStatus entryStatus) {
        ExternalAccount account = getById(externalAccountId);
        if (!BeanValidators.exists(account)) {
            throw new ServiceException("外部账户[" + externalAccountId + "]不存在");
        }
        if (!EAccountStatus.NORMAL.getValue().equals(account.getAccountStatus())) {
            throw new ServiceException("外部账户[" + externalAccountId + "]已冻结");
        }
        ExternalAccountTransDetail transDetail = new ExternalAccountTransDetail();
        transDetail.setExternalAccountId(externalAccountId);
        transDetail.setSrcBillId(transInfo.getSrcBillId());
        transDetail.setSrcBillType(transInfo.getSrcBillType());
        transDetail.setLoanDirection(loanDirection.getValue());
        if (NumberUtil.isLess(transInfo.getTransAmount(), BigDecimal.ZERO)) {
            throw new ServiceException("交易金额异常");
        }
        transDetail.setTransAmount(transInfo.getTransAmount());
        transDetail.setTransTime(transInfo.getTransTime());
        transDetail.setHandlerId(transInfo.getHandlerId());
        transDetail.setEntryStatus(entryStatus.getValue());
        transDetail.setRemark(transInfo.getRemark());
        transDetail.setDelFlag(Constants.UNDELETED);
        return externalAccountTransDetailMapper.insert(transDetail);
    }

}
