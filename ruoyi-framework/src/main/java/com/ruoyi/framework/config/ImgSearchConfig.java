package com.ruoyi.framework.config;

import cn.hutool.core.util.StrUtil;
import com.aliyun.imagesearch20201214.Client;
import com.aliyun.teaopenapi.models.Config;
import com.ruoyi.framework.config.properties.ImgSearchProperties;
import com.ruoyi.framework.img.ImgSearchClientWrapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liangyq
 * @date 2025-04-25 19:51
 */
@EnableConfigurationProperties(ImgSearchProperties.class)
@Configuration
public class ImgSearchConfig {

    @Bean
    public ImgSearchClientWrapper imgSearchClient(ImgSearchProperties properties) throws Exception {
        Config authConfig = new Config();
        authConfig.accessKeyId = properties.getAccessKeyId();
        authConfig.accessKeySecret = properties.getAccessKeySecret();
        authConfig.regionId = properties.getRegionId();
        authConfig.endpoint = properties.getEndPoint();
        if (StrUtil.isNotBlank(properties.getEndpointType())) {
            authConfig.endpointType = properties.getEndpointType();
        }
        return new ImgSearchClientWrapper(new Client(authConfig), properties.getInstanceName());
    }
}
