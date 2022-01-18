package com.ruoyi.api.v1.support;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseLocatorConfig {

    @Bean
    public BaseLocator getBaseLocator() {
        return new BaseLocator();
    }

}
