package com.ruoyi.xkt.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ruoyi.xkt.domain.AlipayCallback;
import com.ruoyi.xkt.mapper.AlipayCallbackMapper;
import com.ruoyi.xkt.service.IAlipayCallbackService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liangyq
 * @date 2025-04-08 17:40
 */
@Service
public class AlipayCallbackServiceImpl implements IAlipayCallbackService {

    @Autowired
    private AlipayCallbackMapper alipayCallbackMapper;


    @Override
    public AlipayCallback getByNotifyId(String notifyId) {
        Assert.notNull(notifyId);
        return alipayCallbackMapper.selectOne(Wrappers.lambdaQuery(AlipayCallback.class)
                .eq(AlipayCallback::getNotifyId, notifyId));
    }

    @Transactional
    @Override
    public int insertAlipayCallback(AlipayCallback alipayCallback) {
        Assert.notNull(alipayCallback);
        return alipayCallbackMapper.insert(alipayCallback);
    }
}
