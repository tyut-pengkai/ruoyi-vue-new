package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.InternalAccount;

import java.util.Collection;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-08 21:14
 */
public interface IInternalAccountService {

    /**
     * 获取内部账户（加锁）
     *
     * @param id
     * @return
     */
    InternalAccount getWithLock(Long id);

    /**
     * 获取内部账户列表（加锁）
     *
     * @param ids
     * @return
     */
    List<InternalAccount> listWithLock(Collection<Long> ids);

}
