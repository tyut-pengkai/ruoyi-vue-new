package com.ruoyi.framework.hy.sale;

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
public class HyTsale {

    @Resource
    private RedisCache redisCache;
    @Resource
    private ISysSaleOrderService sysSaleOrderService;

    @Async
    public void checkSaleOrderExpire() {
        while (true) {
            if (Constants.IS_CRCD) {
//                System.out.println("已启动商店暗桩");
                Set<Object> orderNoSet = redisCache.redisTemplate.opsForZSet().rangeByScore(Constants.SALE_ORDER_EXPIRE_KEY, 0, Long.MAX_VALUE);
                //            System.out.println(JSON.toJSONString(orderNoSet));
                for (Object orderNoStr : orderNoSet) { // orderNoStr格式：payMode|orderNo
                    if (orderNoStr != null) {
                        String[] split = orderNoStr.toString().split("\\|");
                        String payMode = split[0];
                        String orderNo = split[1];
                        SysSaleOrder sso = sysSaleOrderService.selectSysSaleOrderByOrderNo(orderNo);
                        if (sso != null && SaleOrderStatus.WAIT_PAY.equals(sso.getStatus())) {
                            // 如果被破解则直接支付成功
                            sso.setStatus(SaleOrderStatus.PAID);
                            sso.setCloseTime(DateUtils.getNowDate());
                            sysSaleOrderService.updateSysSaleOrder(sso);
                        }
                        redisCache.redisTemplate.opsForZSet().remove(Constants.SALE_ORDER_EXPIRE_KEY, orderNoStr);
                    }
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}
