package com.ruoyi.xkt.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.SimpleEntity;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.xkt.domain.ExternalAccount;
import com.ruoyi.xkt.domain.ExternalAccountTransDetail;
import com.ruoyi.xkt.dto.finance.TransInfo;
import com.ruoyi.xkt.enums.EAccountStatus;
import com.ruoyi.xkt.enums.EEntryStatus;
import com.ruoyi.xkt.enums.EFinBillType;
import com.ruoyi.xkt.enums.ELoanDirection;
import com.ruoyi.xkt.mapper.ExternalAccountMapper;
import com.ruoyi.xkt.mapper.ExternalAccountTransDetailMapper;
import com.ruoyi.xkt.service.IExternalAccountService;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liangyq
 * @date 2025-04-08 21:14
 */
@Slf4j
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
        Assert.notNull(externalAccountId);
        ExternalAccount account = externalAccountMapper.selectById(externalAccountId);
        checkAccountStatus(account);
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
        transDetail.setVersion(0L);
        transDetail.setDelFlag(Constants.UNDELETED);
        return externalAccountTransDetailMapper.insert(transDetail);
    }

    @Transactional
    @Override
    public void entryTransDetail(Long srcBillId, EFinBillType srcBillType) {
        Assert.notNull(srcBillId);
        Assert.notNull(srcBillType);
        List<ExternalAccountTransDetail> transDetails = externalAccountTransDetailMapper.selectList(Wrappers
                .lambdaQuery(ExternalAccountTransDetail.class)
                .eq(ExternalAccountTransDetail::getSrcBillId, srcBillId)
                .eq(ExternalAccountTransDetail::getSrcBillType, srcBillType.getValue())
                .eq(SimpleEntity::getDelFlag, Constants.UNDELETED));
        if (CollUtil.isEmpty(transDetails)) {
            log.warn("单据没有关联的交易明细:{}{}", srcBillType.getLabel(), srcBillId);
            return;
        }
        List<Long> accountIds = transDetails.stream().map(ExternalAccountTransDetail::getExternalAccountId).distinct()
                .collect(Collectors.toList());
        Assert.notEmpty(accountIds);
        Map<Long, ExternalAccount> accountMap = externalAccountMapper.selectByIds(accountIds)
                .stream()
                .collect(Collectors.toMap(SimpleEntity::getId, o -> o));
        //开始入账
        for (ExternalAccountTransDetail transDetail : transDetails) {
            ExternalAccount account = accountMap.get(transDetail.getExternalAccountId());
            checkAccountStatus(account);
            if (EEntryStatus.WAITING.getValue().equals(transDetail.getEntryStatus())) {
                //明细未入账
                transDetail.setEntryStatus(EEntryStatus.FINISH.getValue());
                //更新交易明细
                int r = externalAccountTransDetailMapper.updateById(transDetail);
                if (r == 0) {
                    throw new ServiceException(Constants.VERSION_LOCK_ERROR_COMMON_MSG);
                }
            } else {
                log.warn("单据重复调用入账方法:{}{}", srcBillType.getLabel(), srcBillId);
            }
        }
    }

    /**
     * 检查账户状态
     *
     * @param externalAccount
     */
    private void checkAccountStatus(ExternalAccount externalAccount) {
        Assert.notNull(externalAccount, "账户不存在");
        if (!BeanValidators.exists(externalAccount)) {
            throw new ServiceException("账户[" + externalAccount.getId() + "]不存在");
        }
        if (!EAccountStatus.NORMAL.getValue().equals(externalAccount.getAccountStatus())) {
            throw new ServiceException("账户[" + externalAccount.getId() + "]已冻结");
        }
    }

}
