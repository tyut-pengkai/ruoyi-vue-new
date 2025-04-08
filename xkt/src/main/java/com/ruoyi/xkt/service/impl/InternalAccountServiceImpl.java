package com.ruoyi.xkt.service.impl;

import com.ruoyi.xkt.domain.InternalAccount;
import com.ruoyi.xkt.mapper.InternalAccountMapper;
import com.ruoyi.xkt.mapper.InternalAccountTransDetailMapper;
import com.ruoyi.xkt.service.IInternalAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-08 21:14
 */
@Service
public class InternalAccountServiceImpl implements IInternalAccountService {

    @Autowired
    private InternalAccountMapper internalAccountMapper;
    @Autowired
    private InternalAccountTransDetailMapper internalAccountTransDetailMapper;

    @Transactional
    @Override
    public InternalAccount getWithLock(Long id) {
        return internalAccountMapper.getForUpdate(id);
    }

    @Transactional
    @Override
    public List<InternalAccount> listWithLock(Collection<Long> ids) {
        return internalAccountMapper.listForUpdate(ids);
    }
}
