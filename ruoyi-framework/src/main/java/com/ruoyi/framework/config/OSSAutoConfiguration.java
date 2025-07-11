package com.ruoyi.framework.config;

import com.ruoyi.framework.config.properties.OSSProperties;
import com.ruoyi.framework.oss.OSSClientWrapper;
import com.ruoyi.framework.oss.OSSConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liangyq
 * @date 2025-03-24
 */
@Configuration
@ConditionalOnClass(OSSClientWrapper.class)
@ConditionalOnProperty({"oss.accessKeyId", "oss.accessKeySecret", "oss.endPoint", "oss.bucketName"})
@EnableConfigurationProperties(OSSProperties.class)
public class OSSAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OSSClientWrapper ossClient(OSSProperties properties) {
        OSSConfiguration ossConfiguration = toOSSConfiguration(properties);
        return new OSSClientWrapper(ossConfiguration);
    }

    private OSSConfiguration toOSSConfiguration(OSSProperties properties) {
        OSSConfiguration configuration = new OSSConfiguration();
        configuration.setEndpoint(properties.getEndPoint());
        configuration.setAccessKeyId(properties.getAccessKeyId());
        configuration.setAccessKeySecret(properties.getAccessKeySecret());
        configuration.setBucketName(properties.getBucketName());
        configuration.setPublicBucketName(properties.getPublicBucketName());
        configuration.setRegionId(properties.getRegionId());
        configuration.setRoleArn(properties.getRoleArn());
        configuration.setHttps(properties.isHttps());
        configuration.setExpiredDuration(properties.getExpiredDuration());
        return configuration;
    }
}
