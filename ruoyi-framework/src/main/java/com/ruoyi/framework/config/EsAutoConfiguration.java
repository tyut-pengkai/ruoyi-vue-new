package com.ruoyi.framework.config;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.framework.config.properties.EsProperties;
import com.ruoyi.framework.es.EsClientWrapper;
import com.ruoyi.framework.es.EsConfiguration;
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
@ConditionalOnClass(EsClientWrapper.class)
@ConditionalOnProperty("es.hosts")
@EnableConfigurationProperties(EsProperties.class)
public class EsAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EsClientWrapper esClient(EsProperties properties) {
        EsConfiguration config = BeanUtil.toBean(properties, EsConfiguration.class);
        return new EsClientWrapper(config);
    }
}
