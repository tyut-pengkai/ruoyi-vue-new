package com.ruoyi.framework.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liangyq
 * @date 2025-03-24
 */
@Data
@ConfigurationProperties(prefix = "oss")
public class OSSProperties {

    private String endPoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;

    private boolean https;

    private String regionId;

    private String roleArn;

    private Long expiredDuration;

    private String tempDir;
}
