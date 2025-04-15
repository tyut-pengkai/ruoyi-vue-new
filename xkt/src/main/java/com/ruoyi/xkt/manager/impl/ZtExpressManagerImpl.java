package com.ruoyi.xkt.manager.impl;

import com.ruoyi.xkt.enums.EExpressChannel;
import com.ruoyi.xkt.manager.ExpressManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author liangyq
 * @date 2025-04-15 15:45
 */
@Slf4j
@Component
public class ZtExpressManagerImpl implements ExpressManager {
    @Override
    public EExpressChannel channel() {
        return EExpressChannel.ZT;
    }
}
