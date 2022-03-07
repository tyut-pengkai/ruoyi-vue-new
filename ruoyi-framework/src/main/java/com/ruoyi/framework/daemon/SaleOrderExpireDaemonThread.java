package com.ruoyi.framework.daemon;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.SaleOrderStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.sale.domain.SysSaleOrder;
import com.ruoyi.sale.service.ISysSaleOrderService;
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
    private ISysSaleOrderService saleOrderService;

    @Async
    public void checkSaleOrderExpire() {
        while (true) {
            Set<Object> orderNoSet = redisCache.redisTemplate.opsForZSet().rangeByScore(Constants.SALE_ORDER_EXPIRE_KEY, 0, System.currentTimeMillis());
//            System.out.println(JSON.toJSONString(orderNoSet));
            for (Object orderNo : orderNoSet) {
                if (orderNo != null) {
                    SysSaleOrder order = saleOrderService.selectSysSaleOrderByOrderNo(orderNo.toString());
                    if (order != null && SaleOrderStatus.WAIT_PAY.equals(order.getStatus())) {
                        order.setStatus(SaleOrderStatus.TRADE_CLOSED);
                        order.setCloseTime(DateUtils.getNowDate());
                        saleOrderService.updateSysSaleOrder(order);
                    }
                }
                redisCache.redisTemplate.opsForZSet().remove(Constants.SALE_ORDER_EXPIRE_KEY, orderNo);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
