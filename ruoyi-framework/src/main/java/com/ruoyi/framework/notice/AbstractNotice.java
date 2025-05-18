package com.ruoyi.framework.notice;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.ruoyi.common.core.redis.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author liangyq
 * @date 2025-05-18 17:15
 */
@Slf4j
public abstract class AbstractNotice {

    @Autowired
    protected RedisCache redisCache;

    protected String httpGet(String url, Map<String, String> headers, Map<String, Object> paramMap) {
        HttpRequest httpRequest = HttpUtil.createGet(url);
        if (MapUtil.isNotEmpty(headers)) {
            httpRequest.addHeaders(headers);
        }
        if (MapUtil.isNotEmpty(paramMap)) {
            httpRequest.form(paramMap);
        }
        return httpRequest.execute().body();
    }

    protected String httpPost(String url, Map<String, String> headers, String body) {
        HttpRequest httpRequest = HttpUtil.createPost(url);
        if (MapUtil.isNotEmpty(headers)) {
            httpRequest.addHeaders(headers);
        }
        if (StrUtil.isNotBlank(body)) {
            httpRequest.body(body);
        }
        return httpRequest.execute().body();
    }
}
