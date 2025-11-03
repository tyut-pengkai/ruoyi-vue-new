package com.ruoyi.framework.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author liangyq
 * @date 2025-03-24
 */
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private String version;

    private String downloadUrl;

    private String updateNotes;

    private Boolean forcedUpdate;
}
