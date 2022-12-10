package com.ruoyi.framework.web.service;

import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.AddressUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.service.ISysAppService;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 *
 * @author ruoyi
 */
@Component
public class TokenService {
    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（默认30分钟）
    @Value("${token.expireTime}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Autowired
    private RedisCache redisCache;
    @Resource
    private ISysAppService appService;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            try {
                // 解析对应的权限以及用户信息
                String uuid = tokenToUuid(token);
                Long appUserId = tokenToAppUserId(token);
                String userKey = getTokenKey(uuid);
                if (appUserId != null) {
                    userKey = userKey + "|" + appUserId;
                }
                return redisCache.getCacheObject(userKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
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

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        loginUser.setLoginTime(System.currentTimeMillis());
        setUserAgent(loginUser);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);
        if (loginUser.getIfApp()) {
            if (loginUser.getIfTrial()) {
                claims.put(Constants.APP_USER_ID, loginUser.getAppTrialUserId());
            } else {
                claims.put(Constants.APP_USER_ID, loginUser.getAppUserId());
            }
        }
        return createToken(claims);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (loginUser.getIfApp()) { // 自动刷新APP token
            SysApp app = appService.selectSysAppByAppKey(loginUser.getAppKey());
            Integer heartBeatTime = app.getHeartBeatTime();
            if (heartBeatTime > -1) {
//                if (expireTime - currentTime <= (heartBeatTime * 0.2) * MILLIS_SECOND) {
                refreshToken(loginUser);
//                }
            }
        } else {
            if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
                refreshToken(loginUser);
            }
        }

    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        if (loginUser.getIfApp()) {
            SysApp app = appService.selectSysAppByAppKey(loginUser.getAppKey());
            Integer heartBeatTime = app.getHeartBeatTime();
            // 根据uuid将loginUser缓存
            String userKey = getTokenKey(loginUser.getToken());
            if (loginUser.getIfApp()) {
                if (loginUser.getIfTrial()) {
                    userKey = userKey + "|" + loginUser.getAppTrialUserId();
                } else {
                    userKey = userKey + "|" + loginUser.getAppUserId();
                }
            }
            if (heartBeatTime > -1) {
                loginUser.setExpireTime(System.currentTimeMillis() + heartBeatTime * MILLIS_SECOND);
                redisCache.setCacheObject(userKey, loginUser, heartBeatTime, TimeUnit.SECONDS);
            } else {
                loginUser.setExpireTime(DateUtils.parseDate(UserConstants.MAX_DATE).getTime());
                redisCache.setCacheObject(userKey, loginUser);
            }
        } else {
            loginUser.setExpireTime(System.currentTimeMillis() + expireTime * MILLIS_MINUTE);
            // 根据uuid将loginUser缓存
            String userKey = getTokenKey(loginUser.getToken());
            redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
        }

    }

    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getTokenKey(String uuid) {
        return CacheConstants.LOGIN_TOKEN_KEY + uuid;
    }
}
