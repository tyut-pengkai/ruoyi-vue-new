package com.ruoyi.payment.deamon;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.SaleOrderStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.payment.constants.PaymentDefine;
import com.ruoyi.payment.domain.Payment;
import com.ruoyi.sale.domain.SysSaleOrder;
import com.ruoyi.sale.service.ISysSaleOrderService;
import com.ruoyi.sale.service.ISysSaleShopService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Component
@EnableAsync
public class SaleOrderExpireDaemonThread {

    @Resource
    private RedisCache redisCache;
    @Resource
    private ISysSaleOrderService sysSaleOrderService;
    @Resource
    private ISysSaleShopService sysSaleShopService;

    @Async
    public void checkSaleOrderExpire() {
        while (true) {
            Set<Object> orderNoSet = redisCache.redisTemplate.opsForZSet().rangeByScore(Constants.SALE_ORDER_EXPIRE_KEY, 0, System.currentTimeMillis());
//            System.out.println(JSON.toJSONString(orderNoSet));
            for (Object orderNoStr : orderNoSet) { // orderNoStr格式：payMode|orderNo
                if (orderNoStr != null) {
                    String[] split = orderNoStr.toString().split("\\|");
                    String payMode = split[0];
                    String orderNo = split[1];
                    if (StringUtils.isNotBlank(orderNo)) {
                        SysSaleOrder sso = sysSaleOrderService.selectSysSaleOrderByOrderNo(orderNo);
                        if (sso != null && SaleOrderStatus.WAIT_PAY.equals(sso.getStatus())) {
                            Payment payment = PaymentDefine.paymentMap.get(payMode);
                            if (payment != null) {
                                payment.beforeExpire(sso);
                            }
                        }
                        if (sso != null && SaleOrderStatus.WAIT_PAY.equals(sso.getStatus())) {
                            sso.setStatus(SaleOrderStatus.TRADE_CLOSED);
                            sso.setCloseTime(DateUtils.getNowDate());
                            sysSaleOrderService.updateSysSaleOrder(sso);
                        }
                        redisCache.redisTemplate.opsForZSet().remove(Constants.SALE_ORDER_EXPIRE_KEY, orderNoStr);
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
