package com.ruoyi.xkt.manager.impl;

import com.ruoyi.xkt.enums.EExpressChannel;
import com.ruoyi.xkt.manager.ExpressManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author liangyq
 * @date 2025-04-15 15:45
 */
@Slf4j
@Component
public class ZtExpressManagerImpl implements ExpressManager {

    private static final String CREATE_ORDER_URI = "zto.open.createOrder";

    private static final String STRUCTURE_ADDRESS_URI = "zto.innovate.structureNamePhoneAddress";

    @Value("${zt.appKey:}")
    private String appKey;

    @Value("${zt.appSecret:}")
    private String appSecret;

    @Value("${zt.gatewayUrl:}")
    private String gatewayUrl;

    @Override
    public EExpressChannel channel() {
        return EExpressChannel.ZT;
    }
}
