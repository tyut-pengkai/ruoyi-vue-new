package com.ruoyi.system.service;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisTest {

    @Value("${token.secret}")
    private String secret;

    @Autowired
    private RedisCache redisCache;

    @Test
    public void test() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJsb2dpbl91c2VyX2tleSI6ImM1MWM1ZGE2LWMyOTUtNDBjOS04ZjJlLTQxNGE0MjRhYmJkNyJ9.F5_t3gN02tSQn0SJqqyzjXFiFlzMDsiqYA2dQRaZrQhOVJlWz9wqjDiW_C3AV_5BKBBkLxqo4Tf1e_KkRnUSiQ";
        String uuid = tokenToUuid(token);
        Long appUserId = tokenToAppUserId(token);
        String key = getTokenKey(uuid);
        if (appUserId != null) {
            key = key + "|" + appUserId;
        }
        LoginUser loginUser = null;
//        try {
//            loginUser = (LoginUser) SysCache.get(key);
//        } catch(Exception ignored) {}
        if(loginUser == null) {
            loginUser = redisCache.getCacheObject(key);
//            SysCache.set(key, loginUser, redisCache.getExpire(key));
        }
        System.out.println(JSON.toJSONString(loginUser));
    }

    public String tokenToUuid(String token) {
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = parseToken(token);
            return (String) claims.get(Constants.LOGIN_USER_KEY);
        }
        return null;
    }

    public Long tokenToAppUserId(String token) {
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = parseToken(token);
            Object o = claims.get(Constants.APP_USER_ID);
            if (o != null) {
                if (o instanceof Long) {
                    return (Long) o;
                } else {
                    return Long.parseLong(o.toString());
                }
            }
        }
        return null;
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
    private String getTokenKey(String uuid) {
        return CacheConstants.LOGIN_TOKEN_KEY + uuid;
    }

}