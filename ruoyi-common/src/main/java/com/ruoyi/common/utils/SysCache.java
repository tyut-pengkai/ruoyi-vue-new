package com.ruoyi.common.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p> @Title SysCache
 * <p> @Description 缓存
 */
public class SysCache {

    private static final Logger LOG = LoggerFactory.getLogger(SysCache.class);

    private SysCache() {}

    /** 系统编码 */
    private static Map<String, Object> kv = new ConcurrentHashMap<>();

    static {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(
                new ThreadFactoryBuilder().setNameFormat("SysCache-pool-%d").build());
        // 1天过期
        executor.scheduleAtFixedRate(SysCache::clear, 0, 1, TimeUnit.DAYS);
    }

    /**
     * 根据key查询v
     *
     * @param key 例: userType
     * @param value 数据
     */
    public static void set(String key, Object value) {
        kv.put(key, value);
    }

    /**
     * 根据key查询v
     *
     * @param key 例: userType
     * @return value
     */
    public static Object get(String key) {
//        if(kv.containsKey(key)) {
//            LOG.info("命中缓存：{}", key);
//        }
        return kv.get(key);
    }

    public static Object delete(String key) {
        LOG.info("删除缓存：{}", JSON.toJSONString(kv.get(key)));
        return kv.remove(key);
    }

    public static void clear() {
        if(kv.size() > 0) {
//            LOG.info("缓存清空：{}", JSON.toJSONString(kv));
            LOG.info("缓存清空");
            kv = new ConcurrentHashMap<>();
        }
    }
}
