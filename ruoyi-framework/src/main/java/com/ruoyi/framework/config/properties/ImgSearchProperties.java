package com.ruoyi.framework.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liangyq
 * @date 2025-04-25 19:54
 */
@Data
@ConfigurationProperties(prefix = "img-search")
public class ImgSearchProperties {

    private String accessKeyId;

    private String accessKeySecret;

    private String regionId;

    private String endPoint;

    private String endpointType;

    private String instanceName;
}
