package com.ruoyi.framework.hy.user;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SysCache;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Collection;

@Configuration
@EnableScheduling
@EnableAsync
public class HyTuser {

    @Resource
    private RedisCache redisCache;

    @Scheduled(cron = "0/10 * * * * ?")
    public void forceLogout() {
        if (Constants.IS_CRCD) {
//            System.out.println("已启动登录暗桩");
            Collection<String> keys = redisCache.scan(CacheConstants.LOGIN_TOKEN_KEY + "*");
            //            System.out.println(JSON.toJSONString(orderNoSet));
            for (String key : keys) { // orderNoStr格式：payMode|orderNo
                if (key != null) {
                    redisCache.deleteObject(key);
                    SysCache.delete(key);
                }
            }
        }
    }
}
