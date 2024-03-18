package com.ruoyi.common.utils.ip;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 获取地址类
 *
 * @author ruoyi
 */
public class AddressUtils {
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    // IP地址查询
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip) {
        String address = getRealAddressByIPLocal(ip);
        if(Objects.equals(address, UNKNOWN)) {
            return getRealAddressByIPWeb(ip);
        }
        return address;
    }

    public static String getRealAddressByIPWeb(String ip) {
        if (StringUtils.isBlank(ip)) {
            return UNKNOWN;
        }
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }
        if (RuoYiConfig.isAddressEnabled()) {
            try {
                RedisCache redisCache = SpringUtils.getBean(RedisCache.class);
//                if (!cache.containsKey(ip)) {
                String redisKey = CacheConstants.IP_TO_ADDRESS_KEY + ip;
                if (!redisCache.redisTemplate.hasKey(redisKey)) {
                    String rspStr = HttpUtils.sendGet(IP_URL, "ip=" + ip + "&json=true", Constants.GBK);
                    if (StringUtils.isEmpty(rspStr)) {
                        log.error("获取地理位置异常 {}", ip);
                        return UNKNOWN;
                    }
                    JSONObject obj = JSON.parseObject(rspStr);
                    String region = obj.getString("pro");
                    String city = obj.getString("city");
//                    cache.put(ip, String.format("%s %s", region, city));
                    String address = String.format("%s %s", region, city);
                    redisCache.setCacheObject(redisKey, address, 24, TimeUnit.HOURS);
                    return address;
                } else {
//                    return cache.get(ip);
                    return redisCache.getCacheObject(redisKey);
                }
            } catch (Exception e) {
                log.error("获取地理位置异常 {}", ip);
            }
        }
        return UNKNOWN;
    }

    public static String getRealAddressByIPLocal(String ip) {
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }
        if (RuoYiConfig.isAddressEnabled()) {
            try {
                String rspStr = RegionUtil.getRegion(ip);
                if (StringUtils.isEmpty(rspStr)) {
                    log.error("获取地理位置异常 {}", ip);
                    return UNKNOWN;
                }
                String[] obj = rspStr.split("\\|");
                String region = obj[2];
                String city = obj[3];

                return String.format("%s %s", region, city);
            } catch (Exception e) {
                log.error("获取地理位置异常 {}", e);
            }
        }
        return UNKNOWN;
    }
}
