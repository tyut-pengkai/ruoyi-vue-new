package com.ruoyi.payment.daemon;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.SaleOrderStatus;
import com.ruoyi.sale.domain.SysSaleOrder;
import com.ruoyi.sale.service.ISysSaleOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
public class SaleOrderExpireDaemon implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private RedisCache redisCache;
    @Resource
    private SaleOrderExpireDaemonThread daemonThread;
    @Resource
    private ISysSaleOrderService saleOrderService;

    @SuppressWarnings("unchecked")
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //root application context 没有parent
        ApplicationContext context = event.getApplicationContext().getParent();
        if (context == null) {
            log.info("启动订单监控线程");
            // 获取待支付订单，避免数据库中遗漏订单
            SysSaleOrder order = new SysSaleOrder();
            order.setStatus(SaleOrderStatus.WAIT_PAY);
            List<SysSaleOrder> orders = saleOrderService.selectSysSaleOrderList(order);
            for (SysSaleOrder sso : orders) {
//                sso.setExpireTime(new Date(sso.getCreateTime().getTime() + 5*60*1000));
//                saleOrderService.updateSysSaleOrder(sso);
                redisCache.redisTemplate.opsForZSet().add(Constants.SALE_ORDER_EXPIRE_KEY, sso.getPayMode() + "|" + sso.getOrderNo(), sso.getExpireTime().getTime());
            }
            daemonThread.checkSaleOrderExpire();
            log.info("启动订单监控线程完毕");
        }
    }
}

