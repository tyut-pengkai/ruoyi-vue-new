package com.ruoyi.framework.oss;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-03-24
 */
@Data
public class OSSConfiguration {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String regionId;
    private String roleArn;
    private boolean https;
    private Long expiredDuration;

    @Override
    public OSSConfiguration clone() {
        OSSConfiguration configuration = new OSSConfiguration();
        configuration.setEndpoint(this.endpoint);
        configuration.setAccessKeyId(this.accessKeyId);
        configuration.setAccessKeySecret(this.getAccessKeySecret());
        configuration.setBucketName(this.bucketName);
        configuration.setRegionId(this.regionId);
        configuration.setRoleArn(this.roleArn);
        configuration.setHttps(this.https);
        configuration.setExpiredDuration(this.expiredDuration);
        return configuration;
    }
}
