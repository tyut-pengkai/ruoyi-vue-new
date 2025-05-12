package com.ruoyi.web.controller.common;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.oss.common.auth.Credentials;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.config.properties.OSSProperties;
import com.ruoyi.framework.oss.OSSClientWrapper;
import com.ruoyi.web.controller.common.vo.STSCredentialsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 通用请求处理
 *
 * @author ruoyi
 */
@Api(tags = "通用")
@Slf4j
@RestController
@RequestMapping("/rest/v1/common")
public class CommonController {
    @Autowired
    private OSSClientWrapper ossClient;
    @Autowired
    private OSSProperties ossProperties;
    @Autowired
    private RedisCache redisCache;

    @ApiOperation("获取OSS临时访问凭证")
    @GetMapping("/oss/getCredentials")
    public R<STSCredentialsVO> getCredentials() {
        Long currentUserId = SecurityUtils.getUserId();
        if (currentUserId == null) {
            return R.fail();
        }
        String cacheStr = redisCache.getCacheObject(CacheConstants.USER_STS_KEY + currentUserId);
        if (StrUtil.isNotBlank(cacheStr)) {
            //有缓存直接返回
            return R.ok(JSONUtil.toBean(cacheStr, STSCredentialsVO.class));
        }
        Credentials credentials = ossClient.createStsCredentials();
        STSCredentialsVO vo = new STSCredentialsVO();
        vo.setAccessKeyId(credentials.getAccessKeyId());
        vo.setAccessKeySecret(credentials.getSecretAccessKey());
        vo.setSecurityToken(credentials.getSecurityToken());
        vo.setBucketName(ossProperties.getBucketName());
        vo.setRegionId(ossProperties.getRegionId());
        vo.setEndPoint(ossProperties.getEndPoint());
        vo.setExpiredDuration(ossProperties.getExpiredDuration() - ossProperties.getStsCacheDuration());
        vo.setHttpsFlag(ossProperties.isHttps());
        //缓存
        redisCache.setCacheObject(CacheConstants.USER_STS_KEY + currentUserId, JSONUtil.toJsonStr(vo),
                ossProperties.getStsCacheDuration(), TimeUnit.SECONDS);
        return R.ok(vo);
    }

}
