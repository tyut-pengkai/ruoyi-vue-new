package com.ruoyi.framework.hy.user;

import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.sale.service.ISysSaleOrderService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;

@Component
@EnableAsync
public class HyTuser {

    @Resource
    private RedisCache redisCache;
    @Resource
    private ISysSaleOrderService sysSaleOrderService;

    @Async
    public void forceLogout() {
        while (true) {
            if (Constants.IS_CRCD) {
//                System.out.println("已启动登录暗桩");
                Collection<String> keys = redisCache.keys(Constants.LOGIN_TOKEN_KEY + "*");
                //            System.out.println(JSON.toJSONString(orderNoSet));
                for (String key : keys) { // orderNoStr格式：payMode|orderNo
                    if (key != null) {
                        redisCache.deleteObject(key);
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
