package com.ruoyi.framework.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liangyq
 * @date 2025-03-24
 */
@Data
@ConfigurationProperties(prefix = "es")
public class EsProperties {

    private String hosts;

    private boolean ssl;

    private String username;

    private String password;

    private int maxConnTotal;

    private int maxConnPerRoute;

}
